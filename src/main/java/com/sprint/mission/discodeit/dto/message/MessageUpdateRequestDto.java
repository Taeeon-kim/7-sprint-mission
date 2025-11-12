package com.sprint.mission.discodeit.dto.message;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MessageUpdateRequestDto(
        String content
) {
}
