package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository(Map<UUID, Message> data) {
        this.data = data;
    }

    @Override
    public void save(Message message) {

    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }

    @Override
    public Map<UUID, Message> findAllMap() {
        return Map.of();
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.empty();
    }
}
