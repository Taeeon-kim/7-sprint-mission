package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelRepository extends Repository<Channel, UUID> {
    Map<UUID, Channel> findAllMap();

    List<Channel> findAllByUserId(UUID userId);
}
