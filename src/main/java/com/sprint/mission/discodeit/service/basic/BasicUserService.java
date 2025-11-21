package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.*;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.type.RoleType.USER;

@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserReader userReader;
    private final UserStatusService userStatusService;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;

    public BasicUserService(UserRepository userRepository, UserReader userReader, UserStatusService userStatusService, UserStatusRepository userStatusRepository, BinaryContentRepository binaryContentRepository, BinaryContentService binaryContentService) {
        this.userRepository = userRepository;
        this.userReader = userReader;
        this.userStatusService = userStatusService;
        this.userStatusRepository = userStatusRepository;
        this.binaryContentRepository = binaryContentRepository;
        this.binaryContentService = binaryContentService;
    }

    @Override
    public UserResponseDto signUp(UserSignupCommand userSignupCommand) {
        if (
                userSignupCommand.username() == null ||
                        userSignupCommand.username().isBlank() ||
                        userSignupCommand.password() == null ||
                        userSignupCommand.password().isBlank() ||
                        userSignupCommand.email() == null || userSignupCommand.email().isBlank()

        ) {
            throw new IllegalArgumentException("입력값이 잘못되었습니다.");
        }

        if (userRepository.existsByEmail(userSignupCommand.email()) || userRepository.existsByNickname(userSignupCommand.username())) {
            throw new IllegalArgumentException("이미 사용 중 입니다.");
        }

        UUID profileBinaryId = userSignupCommand.profile().map(binaryContentService::uploadBinaryContent).orElse(null); // NOTE: 서비스 의존은 지양해야하지만, 순환참조 없고 해당 서비스가 다른 서비스에 의존하는게 아니면 공통된건 사용해도 좋고, 오히려 Service라는 이름보단 -Uploader @Component로 구성하는게 나을수도있다.

        User newUser = User.create(userSignupCommand.username(),
                userSignupCommand.email(),
                userSignupCommand.password(),
                USER,
                profileBinaryId
        );
        User savedUser = userRepository.save(newUser);
        // NOTE: user save 이후 userStatus 생성 추가

        // NOTE: 일단 요구사항대로 책임분리 없이 signup에서 userStatus 등록
        UserStatus userStatus = new UserStatus(savedUser.getId());
        userStatusRepository.save(userStatus);
        // TODO: step2: 실패시 처리해야되나? 한다면 유저등록은 되어있기때문에 어떻게 처리할지
        // TODO: 추후 @Trnsactional전 보상로직 try catch 할거 생각
        // TODO: 여기서 이전에 알려준 dispatcher 사용? event 기반? 추후 리펙토링에 추가할것

        return UserResponseDto.from(savedUser, null);
    }

    @Override
    public UserResponseDto getUserById(UUID userId) {

        User user = userReader.findUserOrThrow(userId);

        UserStatus statusByUserId = userStatusRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));

        return UserResponseDto.from(user, statusByUserId.getUserStatus());
    }

    @Override
    public void deleteUser(UUID userId) {
        if (userId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        User user = userReader.findUserOrThrow(userId);
        UserStatus statusByUserId = userStatusRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("해당 유저상태정보가 없습니다."));

        // 정합성, Fk 이유로 유저상태부터 제거
        boolean isRemovedStatus = userStatusRepository.deleteById(statusByUserId.getId());
        if (isRemovedStatus) {
            try {
                boolean deleted = userRepository.deleteById(user.getId());
                if (!deleted) {
                    throw new IllegalStateException("해당 유저를 삭제 하지 못했습니다.");
                }
                if (user.getProfileId() != null) {
                    binaryContentRepository.deleteById(user.getProfileId());
                }

            } catch (Exception e) {
                // 보상로직
                userStatusRepository.save(statusByUserId);
                throw e;
            }
        }

    }

    @Override
    public UserResponseDto updateUser(UserUpdateCommand updateCommand) {
        if (updateCommand.id() == null) { // NOTE: update 는 부분 변경이므로 userId만 가드, 나머지는 Null 허용으로 미변경 정책으로 봄
            // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        User userById = userReader.findUserOrThrow(updateCommand.id());

        UUID profileBinaryId = updateCommand.profile().map(binaryContentService::uploadBinaryContent).orElse(null);

        UserUpdateParams params = UserUpdateParams.from(updateCommand, profileBinaryId); // 경계분리
        userById.update(params);
        userRepository.save(userById);// user repository 사용 책임 분리
        return UserResponseDto.from(userById, null); // NOTE: 멱등성, dirty checking 으로 바뀌던 안바뀌던 해당 객체 반환
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> all = userRepository.findAll();

        return all.stream().map(
                user -> UserDto.from(
                        user,
                        userStatusRepository.findByUserId(user.getId()).orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다.")) // TODO:  N+1 문제 발생, DB 없을때도 이런방식으로 해야되나? 별도 보조인덱스 Map 없는이상 일단 유지, 추후 N+1 개선 신경쓸것
                                .getUserStatus())
        ).toList();

    }

    @Override
    public List<User> getUsersByIds(List<UUID> userIds) {
        if (userIds == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return userRepository.findAllByIds(userIds);
    }
}
