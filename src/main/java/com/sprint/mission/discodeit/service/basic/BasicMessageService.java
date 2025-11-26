package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.MessageReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;
    private final MessageReader messageReader;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;
    private final ReadStatusRepository readStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponseDto> getAllMessages() {
        List<Message> allMessages = messageRepository.findAll();
        return allMessages
                .stream()
                .map(MessageResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public List<MessageResponseDto> getAllMessagesByChannelId(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);
        return messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(MessageResponseDto::from).toList(); // TODO: lazy라 MEssageResponse 내부에서 author, channel N+1 되므로 해결방안 및 심화요구사항 작성
    }

    @Override
    @Transactional(readOnly = true)
    public MessageResponseDto getMessageById(UUID messageId) {
        Message message = messageReader.findMessageOrThrow(messageId);
        return MessageResponseDto.from(message);
    }


    public MessageResponseDto sendMessageToChannel(MessageSendCommand command) {
        if (command.content() == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        // NOTE: 1. 보내려는 유저가 맞는지 확인
        User sender = userReader.findUserOrThrow(command.senderId());
        // NOTE: 2. 보내려는 채널이있는지 확인
        Channel channel = channelReader.findChannelOrThrow(command.channelId());

        // 3. PRIVATE 채널일 때만 멤버 체크
        if (channel.getType() == ChannelType.PRIVATE) {
            boolean isMember = readStatusRepository.existsByUserIdAndChannelId(sender.getId(), channel.getId());
            if (!isMember) {
                throw new IllegalStateException("채널 맴버만 메세지 전송 가능합니다.");
            }
        }

        List<UUID> profileBinaryIds = command.profiles().stream()
                .map(binaryContentService::uploadBinaryContent).toList();

        // UUID → BinaryContent 조회
        List<BinaryContent> attachments = binaryContentRepository.findAllById(profileBinaryIds);
        Message message = Message.builder()
                .content(command.content())
                .author(sender)
                .channel(channel)
                .attachments(attachments)
                .build();

        Message savedMessage = messageRepository.save(message);
        return MessageResponseDto.from(savedMessage);
    }

    @Override
    @Transactional
    public MessageUpdateResponseDto updateMessage(MessageUpdateCommand command) {
        if (command.messageId() == null || command.content() == null || command.content().trim().isEmpty()) {
            throw new IllegalArgumentException("입력값이 잘못되었습니다.");
        }

        Message message = messageReader.findMessageOrThrow(command.messageId());
        boolean isUpdated = false;
        if (!command.content().equals(message.getContent())) {
            isUpdated = message.updateContent(command.content());
        }

        if (isUpdated) {
            Message saved = messageRepository.save(message);
            return MessageUpdateResponseDto.from(saved);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("전달값이 잘못되었습니다.");
        }
        Message message = messageReader.findMessageOrThrow(messageId);
        messageRepository.deleteById(message.getId());
    }
}
