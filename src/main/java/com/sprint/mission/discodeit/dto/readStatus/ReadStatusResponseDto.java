package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusResponseDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant readAt
) {
    public static ReadStatusResponseDto from(ReadStatus readStatus) {
        return ReadStatusResponseDto.builder()
                .id(readStatus.getId())
                .readAt(readStatus.getReadAt())
                .channelId(readStatus.getChannelId())
                .userId(readStatus.getUserId())
                .build();
    }
}
