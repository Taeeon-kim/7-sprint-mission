package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = Objects.requireNonNull(userService, "userService must not be null");
        this.channelService = Objects.requireNonNull(channelService, "channelService must not be null");

        data = new HashMap<>();
    }

    @Override
    public void sendMessageToChannel(UUID channelId, UUID senderId, String content) {
        if (channelId == null || senderId == null || content == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        // NOTE: 1. 보내려는 유저가 맞는지 확인
        User sender = userService.getUserById(senderId);
        // NOTE: 2. 보내려는 채널이있는지 확인
        Channel channel = channelService.getChannel(channelId);
        boolean isMember = channel.isMember(sender.getId());
        if (!isMember) {
            throw new IllegalStateException("채널 맴버만 메세지 전송 가능합니다.");
        }
        Message message = new Message(content, senderId, channelId);
        if (data.containsKey(message.getId())) {
            throw new IllegalStateException("이미 존재하는 메세지 ID가 있습니다.");
        }
        // NOTE: 3. 메세지를 전역 data 인메모리에 저장
        data.put(message.getId(), message);

        // NOTE: 4. 해당 채널에 messageId 추가
        try {
            channel.addMessageId(message.getId());
        } catch (RuntimeException e) {
            data.remove(message.getId()); // NOTE: 보상 트랜잭션 추가
            throw new RuntimeException(e);
        }

//        channel.setUpdatedAt(System.currentTimeMillis()); // NOTE: 보통 메타정보와 메세지 변경을 분리해야하지만 일단 모두 변경으로 인식

    }


    @Override
    public List<Message> getAllMessages() {
        return data.values()
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();
    }

    @Override
    public List<Message> getAllMessagesOfChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channel = channelService.getChannel(channelId);

        return channel.getMessageIds()
                .stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();
    }

    @Override
    public Message getMessageById(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return Optional.ofNullable(data.get(messageId)).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다"));
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        if (messageId == null || content == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Message message = getMessageById(messageId);
        boolean isUpdated = message.updateContent(content);
        if (isUpdated) {
            message.setUpdatedAt(System.currentTimeMillis());
        }


    }

    @Override
    public void deleteMessage(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        data.remove(messageId);
    }


}
