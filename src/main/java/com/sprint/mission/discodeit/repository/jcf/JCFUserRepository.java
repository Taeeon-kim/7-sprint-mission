package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> users = new HashMap<>();

    private JCFUserRepository() {}

    private static final JCFUserRepository INSTANCE = new JCFUserRepository();

    public static JCFUserRepository getInstance(){
        return INSTANCE;
    }

    @Override
    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    @Override
    public User findById(UUID uuid) {
        return users.get(uuid);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void updateNickName(UUID uuid, String newName) {
        User u = users.get(uuid);
        if(u != null) u.setNickName(newName);
    }

    @Override
    public void updatePassword(UUID uuid, String newPassword) {
        User u = users.get(uuid);
        if(u != null) u.setUserPassword(newPassword);
    }

    @Override
    public void delete(UUID uuid) {
        users.remove(uuid);
    }
}
