package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;

public interface MessageRepository extends Repository<Message, UUID>{
    Map<UUID, Message> findAllMap();
}
