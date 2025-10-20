package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messages = new LinkedHashMap<>();

    private JCFMessageRepository() {}

    private static final JCFMessageRepository INSTANCE = new JCFMessageRepository();

    public static JCFMessageRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Message message) {
        messages.put(message.getId(), message);
    }

    @Override
    public Message findByMessage(UUID uuid) {
        return messages.get(uuid);
    }

    @Override
    public List<Message> findAll(User userId) {
        return messages.values().stream()
                .filter(m->m.getSendUser().equals(userId.getId()) || m.getReceiverUser().equals(userId.getId()))
                .sorted(Comparator.comparingLong(Message::getCreateAt))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(UUID uuid, String newMessage) {
        Message m = messages.get(uuid);
        if (m != null) m.setInputMsg(newMessage);
    }

    @Override
    public void deleteMessage(UUID uuid) {
        messages.remove(uuid);
    }
}
