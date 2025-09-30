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
        data.put(user.getId(), user);
    }

    @Override
    public User getUserById(UUID userId) {
        if(userId == null){
            throw new IllegalArgumentException("유저정보가 잘못되었습니다.");
        }
        if (!data.containsKey(userId)) {
            throw new NoSuchElementException("사용자가 없습니다");
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
        return data.values().stream()
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
