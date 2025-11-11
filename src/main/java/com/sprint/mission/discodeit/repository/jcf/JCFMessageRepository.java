package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true // 기본값: jcf
)
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messages = new LinkedHashMap<>();

    @Override
    public Message save(Message message) {
        messages.put(message.getUuid(), message);
        return message;
    }

    @Override
    public Optional<Message> findByMessage(UUID uuid) {
        return Optional.ofNullable(messages.get(uuid));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public List<Message> findAllByChannelId(Channel channel) {
        if(channel == null){ return new ArrayList<>(); }
        return messages.values().stream()
                .filter(m->m.getChannelId().equals(channel.getUuid()))
                .sorted(Comparator.comparing(Message::getCreateAt))
                .toList();
    }

    @Override
    public Optional<Instant> findLastByChannel(UUID channelId) {
        return messages.values().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .map(Message::getCreateAt)
                .max(Comparator.naturalOrder());
    }

    @Override
    public void deleteMessage(UUID uuid) {
        Message message = messages.get(uuid);
        messages.remove(uuid);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {

    }

    //    @Override
//    public void save(Message message) {
//        messages.put(message.getUuid(), message);
//    }
//
//    @Override
//    public Message findByMessage(UUID uuid) {
//        return messages.get(uuid);
//    }
//
//    @Override
//    public List<Message> findUserAll(User user) {
//        return messages.values().stream()
//                .filter(m->m.getUserId().equals(user.getUuid()))
//                .sorted(Comparator.comparing(Message::getCreateAt))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Message> findChannelAll(Channel channel) {
//        return messages.values().stream()
//                .filter(m->m.getChannelId().equals(channel.getUuid()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void deleteMessage(UUID uuid) {
//        messages.remove(uuid);
//    }

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
