package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

import static com.sprint.mission.discodeit.entity.RoleType.USER;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }

    @Override
    public void signUp(String nickname, String email, String pasword, String PhoneNumber) {
        User user = new User(nickname, email, pasword, USER, PhoneNumber);

        User prev = data.put(user.getId(), user); // NOTE: 솔직히 현재로선 UUID 중복될일없음 하지만 추후 실무, 테스트, DB마이그레이션 등 특정 ID를 넣을수있을때 문제라 방어코드
        if (prev != null) {
            throw new IllegalStateException("이미 존재하는 사용자 입니다.: " + user.getId());
        }
    }

    @Override
    public User getUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("유저정보가 잘못되었습니다.");
        }
        return Optional.ofNullable(data.get(userId)).orElseThrow(() -> new NoSuchElementException("사용자가 없습니다"));
    }

    @Override
    public void deleteUser(UUID userId) {
        data.remove(userId);
    }

    @Override
    public void updateUser(UUID userId, String nickname, String email, String password, String phoneNumber) {
        User userById = null;
        try {
            userById = getUserById(userId);
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
        return data.values()
                .stream()
                .toList();
    }

    @Override
    public List<User> getUsersByIds(List<UUID> userIds) {
        return userIds.stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .toList();
    }


}
