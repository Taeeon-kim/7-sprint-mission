package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {

    // 레파지토리 의존성 주입
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) {
        if(userRepository.findById(user.getId()) != null){
            return;
        }
        userRepository.save(user);
        System.out.println("[User 생성 및 저장 완료] : " + user);
    }

    @Override
    public User readUser(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public List<User> readAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID uuid, String newName) {
        userRepository.updateNickName(uuid, newName);
    }

    @Override
    public void updatePw(UUID uuid, String newPassword) {
        userRepository.updatePassword(uuid, newPassword);
    }

    @Override
    public void deleteUser(UUID uuid) {
        userRepository.delete(uuid);
    }
}
