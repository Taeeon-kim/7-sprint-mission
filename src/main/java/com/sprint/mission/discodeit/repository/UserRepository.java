package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);

    void deleteById(UUID id);

    Map<UUID, User> findAllMap();

    List<User> findAll();

    Optional<User> findById(UUID id);

    List<User> findAllByIds(List<UUID> ids);

}
