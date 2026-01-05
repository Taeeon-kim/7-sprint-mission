package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusCreateRequestDto(
       @NotNull UUID userId,
       @NotNull UUID channelId,
        Instant lastReadAt
) {
}
