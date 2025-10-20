package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void createUser(User user); //유저 생성

    User readUser(UUID uuid); //특정 user 조회

    List<User> readAllUser(); //모든 User조회

    void updateUser(UUID uuid, String newName); //User 정보 수정

    void updatePw(UUID uuid, String newPassword); //User 정보 수정

    void deleteUser(UUID uuid); //UUID로 User삭제
}