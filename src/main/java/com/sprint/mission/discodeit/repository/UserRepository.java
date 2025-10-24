package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {

    Map<UUID, User> findAllMap();

    List<User> findAllByIds(List<UUID> ids);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
