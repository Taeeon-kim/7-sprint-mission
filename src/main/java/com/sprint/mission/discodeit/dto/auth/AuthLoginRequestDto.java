package com.sprint.mission.discodeit.dto.auth;

import lombok.Builder;

@Builder
public record AuthLoginRequestDto(
        String username,
        String password
) {
}
