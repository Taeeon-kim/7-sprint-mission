package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.store.Store;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileMessageRepository implements MessageRepository {
    @Override
    public void save(Message message) {
        Map<UUID, Message> allMessages = findAllMap();
        allMessages.put(message.getId(), message);
        Store.saveMap(Store.MESSAGE_DATA_FILE, allMessages);
    }

    @Override
    public boolean deleteById(UUID id) {
        Map<UUID, Message> allMessages = findAllMap();
        Message remove = allMessages.remove(id);
        if (remove != null) {
            Store.saveMap(Store.MESSAGE_DATA_FILE, allMessages);
            return true;
        }
        return false;
    }

    @Override
    public Map<UUID, Message> findAllMap() {
        return Store.loadMap(Store.MESSAGE_DATA_FILE);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Map<UUID, Message> allMessages = findAllMap();
        return Optional.ofNullable(allMessages.get(id));
    }

}
