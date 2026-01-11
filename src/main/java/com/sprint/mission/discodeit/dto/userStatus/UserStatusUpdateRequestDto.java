package com.sprint.mission.discodeit.dto.userStatus;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusUpdateRequestDto(
        @NotNull Instant newLastActiveAt
) {
}

