package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
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
                .id(message.getId())
                .content(message.getContent())
                .authorId(message.getAuthor().getId())
                .channelId(message.getChannel().getId())
                .attachmentIds(message.getAttachments().stream().map(BinaryContent::getId).toList())
                .createdAt(message.getCreatedAt())
                .updatedAt(Instant.now())
                .build();
    }
}
