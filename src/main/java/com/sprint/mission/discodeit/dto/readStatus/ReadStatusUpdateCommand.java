package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateCommand(
        UUID id,
        Instant readAt
) {

    public static ReadStatusUpdateCommand from(UUID id, ReadStatusUpdateRequestDto requestDto) {
        return new ReadStatusUpdateCommand(id, requestDto.newLastReadAt());
    }
}
