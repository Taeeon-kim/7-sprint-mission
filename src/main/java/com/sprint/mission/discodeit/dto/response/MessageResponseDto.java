package com.sprint.mission.discodeit.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class MessageResponseDto {
    private UUID messageId;
    private String content;
    private UUID userId;
    private UUID channelId;
    private List<BinaryContentResponseDto> attachments;
    private Instant createAt;
    private Instant updateAt;
}
