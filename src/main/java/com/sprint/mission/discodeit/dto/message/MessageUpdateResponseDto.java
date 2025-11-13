package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageUpdateResponseDto(
        UUID id,
        UUID channelId,
        UUID authorId,
        String content,
        List<UUID> attachmentIds,
        Instant createdAt,
        Instant updatedAt
) {
    public static MessageUpdateResponseDto from(Message message) {
        return new MessageUpdateResponseDto(
                message.getId(),
                message.getChannelId(),
                message.getSenderId(),
                message.getContent(),
                message.getAttachmentIds() == null ? List.of() : message.getAttachmentIds(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
