package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusResponseDto(
        UUID userId,
        UUID channelId,
        Instant readAt
) {
    public static ReadStatusResponseDto from(ReadStatus readStatus) {
        return ReadStatusResponseDto.builder()
                .readAt(readStatus.getReadAt())
                .channelId(readStatus.getChannelId())
                .userId(readStatus.getUserId())
                .build();
    }
}
