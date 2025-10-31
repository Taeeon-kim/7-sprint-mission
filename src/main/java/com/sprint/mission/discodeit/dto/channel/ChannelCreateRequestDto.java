package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ChannelCreateRequestDto(
        String title,
        String description,
        ChannelType type,
        List<UUID> memberIds
) {
}
