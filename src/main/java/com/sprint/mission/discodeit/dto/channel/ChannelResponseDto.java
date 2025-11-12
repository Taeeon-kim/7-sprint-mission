package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        String name,
        String description,
        Set<UUID> participantIds,
        List<UUID> messageIds,
        ChannelType type,
        Instant lastMessagedAt

) {
    public static ChannelResponseDto from(
            Channel channel,
            Instant lastMessagedAt
    ) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getTitle(),
                channel.getDescription(),
                channel.getUserIds(),
                channel.getMessageIds(),
                channel.getType(),
                lastMessagedAt
        );
    }
}
