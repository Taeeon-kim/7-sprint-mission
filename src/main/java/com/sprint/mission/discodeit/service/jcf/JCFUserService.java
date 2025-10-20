package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final UserRepository repository = JCFUserRepository.getInstance();

    private JCFUserService() {
    }

    private static final JCFUserService INSTANCE = new JCFUserService();

    public static JCFUserService getInstance(){
        return INSTANCE;
    }

    @Override
    public void createUser(User user) {
        repository.save(user);
        System.out.println("[User 생성] : " + user);
    }

    @Override
    public User readUser(UUID uuid) {
        return repository.findById(uuid);
    }

    @Override
    public List<User> readAllUser() {
        return repository.findAll();
    }

    @Override
    public void updateUser(UUID uuid, String newName) {
        repository.updateNickName(uuid, newName);
    }

    @Override
    public void updatePw(UUID uuid, String newPassword) {
        repository.updatePassword(uuid, newPassword);
    }

    @Override
    public void deleteUser(UUID uuid) {
        repository.delete(uuid);
    }
}
