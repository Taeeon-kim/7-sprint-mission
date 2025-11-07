package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ChannelResponseDto(
        UUID channelId,
        String title,
        String description,
        Set<UUID> userIds,
        List<UUID> messageIds,
        UUID createdByUserId,
        ChannelType type,
        Instant currentMessagedAt

) {
    public static ChannelResponseDto from(
            Channel channel,
            Instant currentMessagedAt
    ) {
        return new ChannelResponseDto(channel.getId(),
                channel.getTitle(),
                channel.getDescription(),
                channel.getUserIds(),
                channel.getMessageIds(),
                channel.getCreatedByUserId(),
                channel.getType(),
                currentMessagedAt
        );
    }
}
