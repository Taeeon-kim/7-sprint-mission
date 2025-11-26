package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.*;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserReader userReader;
    private final BinaryContentService binaryContentService;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    @Transactional
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
        BinaryContent binaryContent = (profileBinaryId != null)
                ? binaryContentRepository.getReferenceById(profileBinaryId) // TODO: getReference로 유지할지 findBy로 갈지 검토
                : null;

        User newUser = User.create(userSignupCommand.username(),
                userSignupCommand.email(),
                userSignupCommand.password(),
                binaryContent
        );
        User savedUser = userRepository.save(newUser);
        // NOTE: user save 이후 userStatus 생성 추가

        // NOTE: 일단 요구사항대로 책임분리 없이 signup에서 userStatus 등록
        UserStatus userStatus = new UserStatus(savedUser);
        userStatusRepository.save(userStatus);

        return UserResponseDto.from(savedUser, null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID userId) {

        User user = userReader.findUserOrThrow(userId);

        UserStatus statusByUserId = userStatusRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));

        return UserResponseDto.from(user, statusByUserId.getUserStatus());
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        if (userId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        User user = userReader.findUserOrThrow(userId);
        userRepository.deleteById(user.getId());

    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserUpdateCommand updateCommand) {
        if (updateCommand.id() == null) { // NOTE: update 는 부분 변경이므로 userId만 가드, 나머지는 Null 허용으로 미변경 정책으로 봄
            // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        User userById = userReader.findUserOrThrow(updateCommand.id());

        UUID profileBinaryId = updateCommand.profile().map(binaryContentService::uploadBinaryContent).orElse(null);

        BinaryContent binaryContent = (profileBinaryId != null)
                ? binaryContentRepository.getReferenceById(profileBinaryId) // TODO: getReference로 유지할지 findBy로 갈지 검토
                : null;

        UserUpdateParams params = UserUpdateParams.from(updateCommand, binaryContent); // 경계분리
        userById.update(params);
        userRepository.save(userById);// user repository 사용 책임 분리
        return UserResponseDto.from(userById, null); // NOTE: 멱등성, dirty checking 으로 바뀌던 안바뀌던 해당 객체 반환
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<User> getUsersByIds(List<UUID> userIds) {
        if (userIds == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return userRepository.findAllById(userIds);
    }
}
