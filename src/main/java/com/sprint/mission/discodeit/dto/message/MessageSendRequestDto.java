package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MessageSendRequestDto(
       @NotNull UUID authorId,
       @NotNull UUID channelId,
       @NotBlank String content
) {
}

