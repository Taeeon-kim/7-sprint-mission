package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Optional;

public interface AuthRepository {
    Optional<User> findByUserIdAndPassword(String userId, String password);
}
