package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void save(User user); //유저 저장

    Optional<User> findById(String userId); // 특정 유저 조회

    Optional<User> findById(UUID uuid);

    List<User> findAll(); // 유저 전체 조회

    void delete(UUID uuid); // UUID로 유저 삭제
}