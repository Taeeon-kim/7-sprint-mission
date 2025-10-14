package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.reader.UserReader;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.RoleType.USER;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserReader userReader;

    public BasicUserService(UserRepository userRepository, UserReader userReader) {
        this.userRepository = userRepository;
        this.userReader = userReader;
    }

    @Override
    public void signUp(String nickname, String email, String password, String phoneNumber) {
        if (
                nickname == null ||
                        nickname.isBlank() ||
                        password == null ||
                        password.isBlank() ||
                        email == null || email.isBlank() ||
                        phoneNumber == null ||
                        phoneNumber.isBlank()
        ) {
            throw new IllegalArgumentException("입력값이 잘못되었습니다.");
        }
        User newUser = new User(nickname, email, password, USER, phoneNumber);

        // TODO: 필요하다면 추후 email, phoneNumber 중복 체크하는 정도로, uuid는 결국 항상 false 일거라
        userRepository.save(newUser);
    }

    @Override
    public User getUserById(UUID userId) {
        return userReader.findUserOrThrow(userId);
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
                userById.setUpdatedAt(System.currentTimeMillis());
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
