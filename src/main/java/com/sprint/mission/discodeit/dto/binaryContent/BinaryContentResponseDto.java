package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        String contentType,
        Instant createdAt,
        byte[] bytes
) {
    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getCreatedAt(),
                binaryContent.getBytes());
    }
}
