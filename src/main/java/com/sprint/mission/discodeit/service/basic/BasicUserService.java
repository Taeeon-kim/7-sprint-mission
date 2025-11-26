package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.*;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
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
    private final UserReader userReader;
    private final BinaryContentService binaryContentService;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;


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

        if (userRepository.existsByEmail(userSignupCommand.email()) || userRepository.existsByUsername(userSignupCommand.username())) {
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

        // NOTE: user객체 생성후 userStatus도 넣어서 cascade 영향으로 같이 insert되도록
        newUser.initUserStatus();
        User savedUser = userRepository.save(newUser);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID userId) {

        User user = userReader.findUserOrThrow(userId);

        return userMapper.toDto(user);
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
        return userMapper.toDto(userById); // NOTE: 멱등성, dirty checking 으로 바뀌던 안바뀌던 해당 객체 반환
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {

        List<User> all = userRepository.findAll();

        return all.stream()
                .map(userMapper::toDto)
                .toList();

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
