package com.sprint.mission.discodeit.dto.userStatus;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserStatusResponseDto(UUID id, UUID userId, Instant lastActiveAt) {
}
