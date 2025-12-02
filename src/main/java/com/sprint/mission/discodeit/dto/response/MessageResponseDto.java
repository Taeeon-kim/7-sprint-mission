package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MessageResponseDto {
    private UUID id; //messageId;
    private String content;
    private UUID authorId; //userId;
    private UUID channelId;
    private List<UUID> attachmentIds;
    private Instant createdAt;
    private Instant updatedAt;

    public static MessageResponseDto from (Message message){
        return MessageResponseDto.builder()
                .id(message.getUuid())
                .content(message.getContent())
                .authorId(message.getUserId())
                .channelId(message.getChannelId())
                .attachmentIds(message.getAttachmentIds())
                .createdAt(message.getCreateAt())
                .updatedAt(Instant.now())
                .build();
    }
}
