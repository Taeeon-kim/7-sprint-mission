package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
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
        messages.put(message.getUuid(), message);
    }

    @Override
    public Message findByMessage(UUID uuid) {
        return messages.get(uuid);
    }

    @Override
    public List<Message> findUserAll(User user) {
        return messages.values().stream()
                .filter(m->m.getUserId().equals(user.getUuid()))
                .sorted(Comparator.comparing(Message::getCreateAt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findChannelAll(Channel channel) {
        return messages.values().stream()
                .filter(m->m.getChannelId().equals(channel.getUuid()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMessage(UUID uuid) {
        messages.remove(uuid);
    }

//    @Override
//    public List<Message> findUserAll(User userId) {
//        return messages.values().stream()
//                .filter(m->m.getSenderId().equals(userId.getUuid()))
//                .sorted(Comparator.comparing(Message::getCreateAt))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void updateMessage(UUID uuid, String newMessage) {
//        Message m = messages.get(uuid);
//        if (m != null) m.setInputMsg(newMessage);
//    }
}
