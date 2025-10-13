package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> users = new HashMap<>();

    private JCFUserService() {
    }

    private static final JCFUserService INSTANCE = new JCFUserService();

    public static JCFUserService getInstance(){
        return INSTANCE;
    }

    @Override
    public void createUser(User user) {
        users.put(user.getId(), user);
        System.out.println("[User 생성] : " + user);
    }

    @Override
    public User readUser(UUID uuid) {
        return users.get(uuid);
    }

    @Override
    public List<User> readAllUser() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void updateUser(UUID uuid, String newName) {
        User u = users.get(uuid);
        if( u != null) u.setNickName(newName);
    }

    @Override
    public void updatePw(UUID uuid, String newPassword) {
        User u = users.get(uuid);
        if( u != null ) u.setUserPassword(newPassword);
    }

    @Override
    public void deleteUser(UUID uuid) {
        users.remove(uuid);
    }
}
