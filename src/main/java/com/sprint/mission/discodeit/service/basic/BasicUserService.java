package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.RoleType.USER;

@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserReader userReader;
    private final UserStatusService userStatusService;

    public BasicUserService(UserRepository userRepository, UserReader userReader, UserStatusService userStatusService, UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.userReader = userReader;
        this.userStatusService = userStatusService;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UUID signUp(UserRequestDto request) {
        if (
                request.getNickname() == null ||
                        request.getNickname().isBlank() ||
                        request.getPassword() == null ||
                        request.getPassword().isBlank() ||
                        request.getEmail() == null || request.getEmail().isBlank() ||
                        request.getPhoneNumber() == null ||
                        request.getPhoneNumber().isBlank()
        ) {
            throw new IllegalArgumentException("입력값이 잘못되었습니다.");
        }

        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중 입니다.");
        }

        User newUser = new User(request.getNickname(),
                request.getEmail(),
                request.getPassword(),
                USER,
                request.getPhoneNumber(),
                request.getProfileId());

        User savedUser = userRepository.save(newUser);
        // NOTE: user save 이후 userStatus 생성 추가
        // step1: user UUID 전달 userStatusService.createUserStatus(UUID)
        userStatusService.createUserStatus(newUser.getId());

        // TODO: step2: 실패시 처리해야되나? 한다면 유저등록은 되어있기때문에 어떻게 처리할지, 아래 나와있듯이 책임을 전가하여 구현할것
        // TODO: 여기서 이전에 알려준 dispatcher 사용? event 기반? 추후 리펙토링에 추가할것
        return savedUser.getId();
    }

    @Override
    public UserResponseDto getUserById(UUID userId) {

        User user = userReader.findUserOrThrow(userId);

        UserStatus statusByUserId = userStatusRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));

        return UserResponseDto.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileId(user.getProfileId())
                .role(user.getRole())
                .userStatus(statusByUserId)
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Override
    public void deleteUser(UUID userId) {
        if (userId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUser(UUID userId, String nickname, String email, String password, String phoneNumber) {
        if (userId == null) { // NOTE: update 는 부분 변경이므로 userId만 가드, 나머지는 Null 허용으로 미변경 정책으로 봄
            // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        User userById = null;
        try {
            userById = userReader.findUserOrThrow(userId);
            boolean changeFlag = false;
            changeFlag |= userById.updateNickname(nickname);
            changeFlag |= userById.updateEmail(email);
            changeFlag |= userById.updatePassword(password);
            changeFlag |= userById.updatePhoneNumber(phoneNumber);
            if (changeFlag) {
                userById.setUpdatedAt(Instant.now());
                userRepository.save(userById); // user repository 사용 책임 분리
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByIds(List<UUID> userIds) {
        if (userIds == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return userRepository.findAllByIds(userIds);
    }
}
