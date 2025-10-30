package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
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
        return ChannelResponseDto.builder()
                .channelId(channel.getId())
                .title(channel.getTitle())
                .description(channel.getDescription())
                .userIds(channel.getUserIds())
                .messageIds(channel.getMessageIds())
                .createdByUserId(channel.getCreatedByUserId())
                .type(channel.getType())
                .currentMessagedAt(currentMessagedAt)
                .build();
    }
}
