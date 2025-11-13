package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateResponseDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant readAt,
        Instant createdAt,
        Instant updatedAt

) {
    public static ReadStatusUpdateResponseDto from(ReadStatus readStatus) {
        return new ReadStatusUpdateResponseDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getReadAt(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt());
    }
}
