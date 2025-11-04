package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends Repository<UserStatus, UUID> {
    Optional<UserStatus> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    Map<UUID, UserStatus> findAllMap();
}
