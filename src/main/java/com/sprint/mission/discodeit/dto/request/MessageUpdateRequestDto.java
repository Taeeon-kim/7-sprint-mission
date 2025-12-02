package com.sprint.mission.discodeit.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class MessageUpdateRequestDto {
    private UUID messageId;
    private String content;
}
