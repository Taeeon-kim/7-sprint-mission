package com.sprint.mission.discodeit.dto.userStatus;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class UserStatusResponseDto {
    private final UUID id;
    private final UUID userId;
    private final Instant lastActiveAt;
}
