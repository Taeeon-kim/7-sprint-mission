package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public record MessageResponseDto(
        UUID id,
        UUID channelId,
        UserResponseDto author,
        String content,
        List<BinaryContentResponseDto> attachments,
        Instant createdAt,
        Instant updatedAt

) {

    public static MessageResponseDto from(Message message) {
        UserResponseDto userResponseDto = Optional.ofNullable(message.getAuthor())
                .map(UserResponseDto::from)
                .orElse(null);
        List<BinaryContentResponseDto> binaryContentResponseDtoList = message.getAttachments()
                .stream()
                .map(BinaryContentResponseDto::from)
                .toList();

        return new MessageResponseDto(
                message.getId(),
                message.getChannel().getId(),
                userResponseDto,
                message.getContent(),
                binaryContentResponseDtoList,
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
