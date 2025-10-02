package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.store.Store;

import java.util.*;

import static com.sprint.mission.discodeit.entity.RoleType.USER;

public class FileUserService implements UserService {

    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        User user = new User(nickname, email, password, USER, phoneNumber);
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 사용자 입니다.: " + user.getId());
        }
        userRepository.save(user); // create userRepository 사용 책임 분리
    }

    @Override
    public User getUserById(UUID userId) {
        if (userId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("사용자가 없습니다"));
    }

    @Override
    public void deleteUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다."); // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
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
            userById = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("사용자가 없습니다"));//  repository 사용 책임 분리
            boolean changeFlag = false;
            changeFlag |= userById.updateNickname(nickname);
            changeFlag |= userById.updateEmail(email);
            changeFlag |= userById.updatePassword(password);
            changeFlag |= userById.updatePhoneNumber(phoneNumber);
            if (changeFlag) {
                userById.setUpdatedAt(System.currentTimeMillis());
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
