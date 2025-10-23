package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    // 레파지토리 의존성 주입
    private final UserRepository userRepository;

    //CRUD
    @Override
    public void createUser(User user) {
        if(userRepository.findById(user.getUuid()) != null){
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
    public void updatePassword(UUID uuid, String newPassword) {
        userRepository.updatePassword(uuid, newPassword);
    }

    @Override
    public void deleteUser(UUID uuid) {
        userRepository.delete(uuid);
    }

    //작동 테스트
    public User[] runUserService(){
        // User 등록
        User[] users = {
                new User("test00", "pass123", "Alice"),
                new User("test02", "0000pass", "Bob"),
                new User("test03", "12341234", "Chily"),
                new User("test05", "pw123456", "Tom")
        };
        for (User u : users) {
            createUser(u);
        }

        // 유저 전체 조회
        userList();

        // 유저 닉네임 수정 Bob->Minsu
        updateUser(users[1].getUuid(), "Minsu");

        // 유저 password 수정 Bob : 0000pass -> 012456pw
        updatePassword(users[1].getUuid(), "012456pw");

        // 유저 단건 조회
        System.out.println("[유저 검색] : " + readUser(users[1].getUuid()));

        // 유저 삭제
        deleteUser(users[3].getUuid());
        System.out.println("탈퇴 : " + users[3].getNickName() + "님");

        // 전체 조회
        userList();

        return users;
    }

    //유저 전체 조회
    public void userList() {
        System.out.println("[유저 전체 조회]");
        Set<String> userSet = new HashSet<>();
        for (User u : readAllUser()) {
            if (userSet.add(u.getUserId())) { // userId 기준
                System.out.println("ID: " + u.getUserId() + " / Name: " + u.getNickName());
            }
        }
    }
}
