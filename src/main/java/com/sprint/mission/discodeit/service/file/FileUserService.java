package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {

    // 레파지토리 의존성 주입
    private final UserRepository userRepository = FileUserRepository.getInstance();

    //싱글톤
    private static final FileUserService INSTANCE = new FileUserService();

    private FileUserService(){

    }

    public static FileUserService getInstance(){
        return INSTANCE;
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
