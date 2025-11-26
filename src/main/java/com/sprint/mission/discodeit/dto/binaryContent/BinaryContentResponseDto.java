package com.sprint.mission.discodeit.dto.binaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        String contentType,
        Instant createdAt,
        byte[] bytes
) {
}
