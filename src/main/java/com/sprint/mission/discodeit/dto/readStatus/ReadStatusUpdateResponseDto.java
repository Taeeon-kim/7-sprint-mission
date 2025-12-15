package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateResponseDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt

) {
    public static ReadStatusUpdateResponseDto from(ReadStatus readStatus) {
        return new ReadStatusUpdateResponseDto(
                readStatus.getId(),
                readStatus.getUser().getId(),
                readStatus.getChannel().getId(),
                readStatus.getLastReadAt()
        );
    }
}
