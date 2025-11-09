package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MessageResponseDto {
    private UUID messageId;
    private String content;
    private UUID userId;
    private UUID channelId;
    private List<UUID> attachments;
    private Instant createAt;
    private Instant updateAt;

    private static MessageResponseDto from (Message message){
        return MessageResponseDto.builder()
                .messageId(message.getUuid())
                .content(message.getContent())
                .userId(message.getUserId())
                .channelId(message.getChannelId())
                .attachments(message.getAttachmentIds())
                .createAt(message.getCreateAt())
                .updateAt(Instant.now())
                .build();
    }
}
