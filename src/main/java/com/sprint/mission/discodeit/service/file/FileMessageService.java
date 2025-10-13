package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class FileMessageService implements MessageService {

    private final ChannelService channelService;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    public FileMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository, ChannelRepository channelRepository) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
    }


    @Override
    public List<Message> getAllMessages() {
        Map<UUID, Message> allMessages = messageRepository.findAllMap();
        return allMessages.values()
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
        if (messageId == null) throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        return messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));
    }

    public void sendMessageToChannel(UUID channelId, UUID senderId, String content) { // TODO: 추후 컨트롤러 계층생성시 파라미터를 DTO로 변경(파라미터가 길어질시)
        if (channelId == null || senderId == null || content == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
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

        // NOTE: 3. 메세지를 전역 Message 저장소에 저장
        messageRepository.save(message);
        // NOTE: 4. 해당 채널에 messageId 추가 및 업데이트
        channel.addMessageId(message.getId());
        channelRepository.save(channel);

//        channel.setUpdatedAt(System.currentTimeMillis()); // NOTE: 보통 메타정보와 메세지 변경을 분리해야하지만 일단 모두 변경으로 인식

    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        if (messageId == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("입력값이 잘못되었습니다.");
        }

        Message message = getMessageById(messageId);
        boolean isUpdated = false;
        if (!content.equals(message.getContent())) {
            isUpdated = message.updateContent(content);
        }

        if (isUpdated) {
            message.setUpdatedAt(System.currentTimeMillis());
            messageRepository.save(message);
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("전달값이 잘못되었습니다.");
        }
        Message message = getMessageById(messageId);
        boolean isDeleted = messageRepository.deleteById(messageId);
        if (isDeleted) {
            Channel channel = channelRepository.findById(message.getChannelId())
                    .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));
            channel.removeMessageId(messageId);
            channelRepository.save(channel);
        }
    }
}
