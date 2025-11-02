package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public boolean deleteById(UUID id) {
        Message remove = data.remove(id);
        return remove != null;
    }

    @Override
    public List<Message> findAll() {
        return findAllMap()
                .values()
                .stream()
                .toList();
    }

    @Override
    public Map<UUID, Message> findAllMap() {
        return data;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }
}
