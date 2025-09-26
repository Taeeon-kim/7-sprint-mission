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
        return Optional.ofNullable(data.get(userId)).orElseThrow(()-> new NoSuchElementException("사용자가 없습니다"));
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
            userById.updateNickname(user.getNickname());
            userById.updateEmail(user.getEmail());
            userById.updatePassword(user.getPassword());
            userById.updatePhoneNumber(user.getPhoneNumber());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        return data.values().stream()
                .toList();
    }
}
