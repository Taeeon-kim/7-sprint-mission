package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    //의존성 주입
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message createMessage(MessageCreateRequestDto messageCreateRequestDto) {
        channelRepository.findByChannel(messageCreateRequestDto.getChannelId())
                .orElseThrow(() -> new IllegalStateException("채널정보를 찾을 수 없습니다."));
        if (userRepository.findById(messageCreateRequestDto.getUserId()) == null)
            throw new IllegalStateException("작성자가 없습니다.");

        Message message = new Message(
                messageCreateRequestDto.getChannelId(),
                messageCreateRequestDto.getUserId(),
                messageCreateRequestDto.getContent(),
                messageCreateRequestDto.getAttachmentIds() != null ? messageCreateRequestDto.getAttachmentIds() : new ArrayList<>()
        );
        return messageRepository.save(message);
    }

    @Override
    public Message findByMessage(UUID uuid) {
        return messageRepository.findByMessage(uuid)
                .orElse(null); //.orElseThrow(()->new IllegalStateException("메시지를 찾을 수 없습니다."));
    }

    @Override
    public List<Message> findUserAllMessage(User users) {
        if (users == null) {
            throw new IllegalStateException("유저 정보가 없습니다.");
        }

        return messageRepository.findAll().stream()
                .filter(m -> m.getUserId().equals(users.getUuid()))
                .sorted(Comparator.comparing(Message::getCreateAt))
                .toList();
    }

    @Override
    public List<Message> findChannelAllMessage(Channel channels) {
        if (channels == null) {
            throw new IllegalArgumentException("채널 정보가 없습니다.");
        }
        return messageRepository.findAllByChannelId(channels);
    }

    @Override
    public void updateMessage(MessageUpdateRequestDto messageUpdateRequestDto) {
        Message message = messageRepository.findByMessage(messageUpdateRequestDto.getMessageId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 메시지를 찾을 수 없습니다."));

        message.setUpdate(messageUpdateRequestDto.getContent());

        List<UUID> attachments = message.getAttachmentIds();
        if (attachments != null) {
            for (UUID attachmentId : attachments) {
                binaryContentRepository.findById(attachmentId);
            }
            message.setAttachmentIds(new ArrayList<>());
        }
        messageRepository.save(message);
        System.out.println("[Message 수정] : " + message.getContent());
    }

    @Override
    public void deleteMessage(UUID uuid) {
        Message message = messageRepository.findByMessage(uuid)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지를 찾을 수 없습니다."));

        List<UUID> attachments = message.getAttachmentIds();
        if (attachments != null) {
            for (UUID attachmentId : attachments) {
                binaryContentRepository.delete(attachmentId);
            }
        }

        messageRepository.deleteMessage(uuid);
        System.out.println("[Message 삭제] : " + messageRepository.findByMessage(uuid));
    }

    public void runMessageTest() {
        System.out.println("----Message Service Test----");
        List<User> users = userRepository.findAll();
        List<Channel> channels = channelRepository.findAll();

        if (users.isEmpty() || channels.isEmpty()) {
            System.out.println("유저나 채널이 존재하지 않습니다.");
            return;
        }

        List<MessageCreateRequestDto> messageCreateRequestDtoList = new ArrayList<>();

        List<UUID> attachmentIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        messageCreateRequestDtoList.add(new MessageCreateRequestDto(
                users.get(0).getUuid(), users.get(0).getUserName(),
                channels.get(0).getUuid(), channels.get(0).getChannelName() +
                "(" + channels.get(0).getChannelType() + ")",
                users.get(0).getUserName() + "의 메시지", attachmentIds
        ));
        messageCreateRequestDtoList.add(new MessageCreateRequestDto(
                users.get(1).getUuid(), users.get(1).getUserName(),
                channels.get(1).getUuid(), channels.get(1).getChannelName() +
                "(" + channels.get(1).getChannelType() + ")",
                users.get(1).getUserName() + "의 메시지", null
        ));
        messageCreateRequestDtoList.add(new MessageCreateRequestDto(
                users.get(2).getUuid(), users.get(2).getUserName(),
                channels.get(2).getUuid(), channels.get(2).getChannelName() +
                "(" + channels.get(2).getChannelType() + ")",
                users.get(2).getUserName() + "의 메시지", null
        ));

        for (MessageCreateRequestDto messageCreateRequestDto : messageCreateRequestDtoList) {
            List<UUID> attachments = messageCreateRequestDto.getAttachmentIds();
            if (attachments == null) attachments = new ArrayList<>();
            Message message = createMessage(messageCreateRequestDto);
            System.out.println("[Message] : " + userRepository.findById(messageCreateRequestDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("유저 없음")).getUserName()
                    + " : " + messageCreateRequestDto.getChannelName()
                    + " : " + messageCreateRequestDto.getContent()
                    + " | 첨부파일 수: " + attachments.size());
        }

        System.out.println("채널별 조회");
        for (Channel channel : channels) {
            List<Message> channelMessages = findChannelAllMessage(channel);
            System.out.println("채널명 : " + channel.getChannelName() +
                    "(" + channels.get(2).getChannelType() + ")");
            if (channelMessages.isEmpty()) {
                System.out.println("메시지 없음");
            } else {
                for (Message message : channelMessages) {
                    System.out.println(" - " + message.getContent() + " / 첨부파일 수 : " + message.getAttachmentIds().size());
                }
            }
        }
        System.out.println("특정 유저 메시지 조회");
        for (User user : users) {
            List<Message> userMessages = findUserAllMessage(user);
            if (!userMessages.isEmpty()) {
                System.out.println(userMessages.get(0).getChannelId() + " : " + userMessages.get(0).getContent());
            }
        }

        System.out.println("Update Test");
        Message firstMessage = findUserAllMessage(users.get(0)).stream().findFirst().orElse(null);
        MessageUpdateRequestDto messageUpdateRequestDto = new MessageUpdateRequestDto(firstMessage.getUuid(), "업데이트 된 메시지");
        updateMessage(messageUpdateRequestDto);
        System.out.println("[Update] : " + findByMessage(firstMessage.getUuid()).getContent());

        System.out.println("Delete Test");
        Message secondMessage = findUserAllMessage(users.get(1)).get(0);
        deleteMessage(secondMessage.getUuid());

        for (Channel channel : channels) {
            List<Message> channelMessages = findChannelAllMessage(channel);
            System.out.println("채널명 : " + channel.getChannelType() + "(" + channel.getChannelName() + ")");
            if (channelMessages.isEmpty()) {
                System.out.println("메시지 없음");
            } else {
                for (Message message : channelMessages) {
                    System.out.println(" / " + message.getContent() + " / 첨부파일 수 : " + message.getAttachmentIds().size());
                }
            }
        }
    }
}
