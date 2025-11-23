package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record MessageResponseDto(
        UUID id,
        UUID channelId,
        UUID authorId,
        String content,
        List<UUID> attachmentIds,
        Instant createdAt,
        Instant updatedAt

) {

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getChannel().getId(),
                message.getAuthor().getId(),
                message.getContent(),
                message.getAttachments() == null ? List.of() :
                        message.getAttachments().stream()
                                .map(attachment -> attachment.getId())
                                .toList(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
