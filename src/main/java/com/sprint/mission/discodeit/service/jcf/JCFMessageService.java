package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.MessageReader;
import com.sprint.mission.discodeit.service.reader.UserReader;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final UserReader userReader;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;
    private final ChannelReader channelReader;
    private final MessageReader messageReader;

    public JCFMessageService(ChannelService channelService, UserReader userReader, MessageRepository messageRepository, ChannelReader channelReader, MessageReader messageReader) {

        this.channelService = Objects.requireNonNull(channelService, "channelService must not be null");
        this.userReader = userReader;
        this.messageRepository = messageRepository;
        this.channelReader = channelReader;
        this.messageReader = messageReader;
    }

    @Override
    public void sendMessageToChannel(UUID channelId, UUID senderId, String content) { // TODO: 추후 컨트롤러 계층생성시 파라미터를 DTO로 변경(파라미터가 길어질시)
        if (content == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        // NOTE: 1. 보내려는 유저가 맞는지 확인
        User sender = userReader.findUserOrThrow(senderId);
        // NOTE: 2. 보내려는 채널이있는지 확인
        Channel channel = channelReader.findChannelOrThrow(channelId);
        boolean isMember = channel.isMember(sender.getId());
        if (!isMember) {
            throw new IllegalStateException("채널 맴버만 메세지 전송 가능합니다.");
        }
        Message message = new Message(content, sender.getId(), channel.getId(), null);

        messageRepository.save(message);

        // NOTE: 4. 해당 채널에 messageId 추가
        try {
            channel.addMessageId(message.getId());
        } catch (RuntimeException e) {
            messageRepository.deleteById(message.getId()); // NOTE: 보상 트랜잭션 추가
            throw new RuntimeException(e);
        }

//        channel.setUpdatedAt(System.currentTimeMillis()); // NOTE: 보통 메타정보와 메세지 변경을 분리해야하지만 일단 모두 변경으로 인식

    }


    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAllMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();
    }

    @Override
    public List<Message> getAllMessagesOfChannel(UUID channelId) {
        if (channelId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channel = channelService.getChannel(channelId);
        Map<UUID, Message> allMessages = messageRepository.findAllMap();
        return channel.getMessageIds()
                .stream()
                .map(allMessages::get)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();
    }

    @Override
    public Message getMessageById(UUID messageId) {
        return messageReader.findMessageOrThrow(messageId);
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        if (content == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Message message = messageReader.findMessageOrThrow(messageId);
        boolean isUpdated = message.updateContent(content);
        if (isUpdated) {
            message.setUpdatedAt(System.currentTimeMillis());
        }


    }

    @Override
    public void deleteMessage(UUID messageId) {
        if (messageId == null) { // TODO: 추후 컨트롤러에서 체크
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        messageRepository.deleteById(messageId);
    }


}
