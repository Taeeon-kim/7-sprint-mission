package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.store.Store;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    @Override
    public void save(Message message) {
        Map<UUID, Message> allMessages = findAllMap();
        allMessages.put(message.getId(), message);
        Store.saveMap(Store.MESSAGE_DATA_FILE, allMessages);
    }

    @Override
    public Map<UUID, Message> findAllMap() {
        return Store.loadMap(Store.MESSAGE_DATA_FILE);
    }

}
