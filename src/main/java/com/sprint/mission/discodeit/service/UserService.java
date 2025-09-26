package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 회원가입
    public void signUp(String nickname, String email, String password, String phoneNumber);
    // 회원정보 읽기
    public User getUserById(UUID userId);
    // 회원탈퇴
    public void deleteUser(UUID userId);
    // 정보수정
    public void updateUser(UUID userId, String nickname, String email, String phoneNumber);

    // 모든 유저리스트 읽기(관리측면 메서드)
    public List<User> getAllUsers();

}
