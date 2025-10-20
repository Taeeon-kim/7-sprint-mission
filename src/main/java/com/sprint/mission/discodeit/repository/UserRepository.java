package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    void save(User user); //유저 저장

    User findById(UUID uuid); // 특정 유저 조회

    List<User> findAll(); // 유저 전체 조회

    void updateNickName(UUID uuid, String newName); // 유저 닉네임 수정

    void updatePassword(UUID uuid, String newPassword); // 유저 pw 수정

    void delete(UUID uuid); // UUID로 유저 삭제
}