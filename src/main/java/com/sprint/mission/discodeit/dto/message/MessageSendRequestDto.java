package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MessageSendRequestDto(
       @NotBlank UUID authorId,
       @NotBlank UUID channelId,
       @NotBlank String content
) {
}

