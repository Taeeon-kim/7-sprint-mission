package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReadStatusRepository extends Repository<ReadStatus, UUID> {
    List<ReadStatus> findByChannelId(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);

    Map<UUID, ReadStatus> findAllMap();

}
