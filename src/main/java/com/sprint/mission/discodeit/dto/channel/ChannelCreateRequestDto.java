package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ChannelCreateRequestDto(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 500) String description,
        List<UUID> participantIds
) {
}
