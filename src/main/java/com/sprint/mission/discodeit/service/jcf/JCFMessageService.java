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
        this.userService = userService;
        this.channelService = channelService;
        data = new HashMap<>();
    }

    @Override
    public void sendMessage(Message message, UUID channelId, UUID senderId) {
        if (message == null) {
            throw new IllegalArgumentException("메세지 정보가 잘못되었습니다.");
        }
        // NOTE: 1. 보내려는 유저가 맞는지 확인
        User sender = userService.getUserById(senderId);
        // NOTE: 2. 보내려는 채널이있는지 확인
        Channel channel = channelService.getChannel(channelId);
        boolean isMember = channel.isMember(senderId);
        if (!isMember) {
            throw new IllegalStateException("채널 맴버만 메세지 전송 가능합니다.");
        }
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

        channel.setUpdatedAt(System.currentTimeMillis()); // NOTE: 보통 메타정보와 메세지 변경을 분리해야하지만 일단 모두 변경으로 인식

    }

//    @Override
//    public List<Message> getAllMessagesByIds(List<UUID> messageIds) {
//        return messageIds.stream()
//                .map(data::get)
//                .filter(Objects::nonNull) // NOTE: nonNull 처리가 실제 서비스에서 필요한지 아니면 다른 처리가 필요한지 고려
//                .toList();
//    }

    @Override
    public List<Message> getAllMessagesOfChannel(UUID channelId) {
        Channel channel = channelService.getChannel(channelId);

        return channel.getMessageIds()
                .stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .toList();
    }


}
