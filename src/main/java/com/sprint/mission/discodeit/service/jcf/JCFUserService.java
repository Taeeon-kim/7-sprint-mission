package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }

    @Override
    public void signUp(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User getUserById(UUID userId) {
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
    public void updateUser(User user) {
        User userById = null;
        try {
            userById = getUserById(user.getId());
            boolean changeFlag = false;
            changeFlag |= userById.updateNickname(user.getNickname());
            changeFlag |= userById.updateEmail(user.getEmail());
            changeFlag |= userById.updatePassword(user.getPassword());
            changeFlag |= userById.updatePhoneNumber(user.getPhoneNumber());
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
