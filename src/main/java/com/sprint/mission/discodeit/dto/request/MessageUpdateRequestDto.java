package com.sprint.mission.discodeit.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageUpdateRequestDto {
    private UUID messageId;
    private String content;

    public MessageUpdateRequestDto(UUID messageId, String content) {
        this.messageId = messageId;
        this.content = content;
    }
}
