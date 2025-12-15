package com.sprint.mission.discodeit.dto.binaryContent;

import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        String contentType,
        Long size
) {
}
