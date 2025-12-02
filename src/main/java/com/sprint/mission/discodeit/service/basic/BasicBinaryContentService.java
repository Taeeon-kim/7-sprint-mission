package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        BinaryContent entity = new BinaryContent(
                binaryContentCreateRequestDto.getFileName(),
                binaryContentCreateRequestDto.getContentType(),
                binaryContentCreateRequestDto.getBytes()
        );
        binaryContentRepository.save(entity);
        return BinaryContentResponseDto.from(entity);
    }

    @Override
    public BinaryContentResponseDto find(UUID uuid) {
        BinaryContent entity = binaryContentRepository.findById(uuid);
        if (entity == null) {
            throw new RuntimeException("Binary content를 찾을 수 없음");
        }
        return BinaryContentResponseDto.from(entity);
    }

    @Override
    public List<BinaryContentResponseDto> findByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        UUID profileId = user.getProfileImageId();

        if(profileId==null){
            return List.of();
        }
        BinaryContent content = binaryContentRepository.findById(profileId);
        if(content==null){
            return List.of();
        }

        return List.of(BinaryContentResponseDto.from(content));
    }

    @Override
    public List<BinaryContentResponseDto> findAll() {
        return binaryContentRepository.findAll().stream()
                .map(BinaryContentResponseDto::from)
                .toList();
    }

    @Override
    public List<BinaryContentResponseDto> findByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findByChannelId(channelId);

        List<UUID> attachmentIds = messages.stream()
                .flatMap(message->message.getAttachmentIds().stream())
                .toList();

        if(attachmentIds.isEmpty()){
            return List.of();
        }

        return attachmentIds.stream()
                .map(binaryContentRepository::findById)
                .filter(Objects::nonNull)
                .map(BinaryContentResponseDto::from)
                .toList();
    }

    @Override
    public void delete(UUID uuid) {
        BinaryContent binaryContent = binaryContentRepository.findById(uuid);
        if (binaryContent == null) {
            throw new RuntimeException("BinaryContent를 찾을 수 없다.");
        }
        binaryContentRepository.delete(uuid);
    }
}
