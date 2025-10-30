package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record ReadStatusCreateRequestDto(
        UUID userId,
        UUID channelId
) {
}
