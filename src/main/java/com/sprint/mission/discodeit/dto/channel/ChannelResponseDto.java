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
        List<UUID> participantIds,
        List<UUID> messageIds,
        ChannelType type,
        Instant lastMessagedAt
) {
    public static ChannelResponseDto from(
            Channel channel,
            List<UUID> participantIds,
            List<UUID> messageIds,
            Instant lastMessagedAt
    ) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                participantIds,
                messageIds,
                channel.getType(),
                lastMessagedAt

        );
    }
}
