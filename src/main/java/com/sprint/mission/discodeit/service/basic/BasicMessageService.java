package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public Message createMessage(MessageCreateRequestDto messageCreateRequestDto,
                                 List<MultipartFile> files) {
        channelRepository.findByChannel(messageCreateRequestDto.getChannelId())
                .orElseThrow(() -> new IllegalStateException("채널정보를 찾을 수 없습니다."));
        if (userRepository.findById(messageCreateRequestDto.getUserId()) == null)
            throw new IllegalStateException("작성자가 없습니다.");

        // attachmentIds + 새로 업로드한 파일UUID
        List<UUID> attachmentIds = messageCreateRequestDto.getAttachments() != null
                ? new ArrayList<>(messageCreateRequestDto.getAttachments())
                : new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) continue;
                BinaryContent saved;
                try {
                    saved = binaryContentRepository.save(
                            new BinaryContent(file.getOriginalFilename(), file.getContentType(), file.getBytes()));
                    attachmentIds.add(saved.getUuid());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("파일 저장 실패", e);
                }
            }
        }

        Message message = new Message(
                messageCreateRequestDto.getChannelId(),
                messageCreateRequestDto.getUserId(),
                messageCreateRequestDto.getContent(),
                attachmentIds
//                messageCreateRequestDto.getAttachmentIds() != null ? messageCreateRequestDto.getAttachmentIds() : new ArrayList<>()
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
    public Message updateMessage(MessageUpdateRequestDto messageUpdateRequestDto,
                                 List<MultipartFile> files) {
        Message message = messageRepository.findByMessage(messageUpdateRequestDto.getMessageId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 메시지를 찾을 수 없습니다."));

        message.setUpdate(messageUpdateRequestDto.getContent());

        List<UUID> attachments = message.getAttachmentIds() != null
                ? new ArrayList<>(message.getAttachmentIds())
                : new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        BinaryContent saved = binaryContentRepository.save(
                                new BinaryContent(file.getOriginalFilename(), file.getContentType(), file.getBytes())
                        );
                        attachments.add(saved.getUuid());
                        System.out.println("[파일 저장 완료] : " + file.getOriginalFilename());
                    } catch (Exception e) {
                        throw new RuntimeException("파일 저장 실패", e);
                    }
                }
            }
        }
        message.setAttachmentIds(attachments);
        System.out.println("[Message 수정] : " + message.getContent());
        return messageRepository.save(message);
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
}
