package com.sprint.mission.discodeit.dto.message;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record MessageSendRequestDto(
        UUID senderId,
        String content,
        List<UUID> binaryFileIds
) {
}

