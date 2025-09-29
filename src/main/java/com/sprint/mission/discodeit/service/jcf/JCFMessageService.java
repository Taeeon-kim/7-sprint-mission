package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        data = new HashMap<>();
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public List<Message> getAllMessagesByIds(List<UUID> messageIds) {
        return List.of();
    }
}
