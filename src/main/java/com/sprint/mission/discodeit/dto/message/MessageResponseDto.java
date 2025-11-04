package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;

import java.util.List;
import java.util.UUID;


public record MessageResponseDto(
        UUID id,
        UUID channelId,
        UUID senderId,
        String content,
        List<UUID> attachmentIds
) {

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getChannelId(),
                message.getSenderId(),
                message.getContent(),
                message.getAttachmentIds() == null ? List.of() : message.getAttachmentIds()
        );
    }
}
