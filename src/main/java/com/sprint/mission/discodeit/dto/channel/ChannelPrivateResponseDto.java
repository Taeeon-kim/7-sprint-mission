package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record ChannelPrivateResponseDto( // public은 userIds 생략
                                         UUID channelId,
                                         String title,
                                         String description,
                                         Set<UUID> userIds,
                                         List<UUID> messageIds,
                                         UUID createdByUserId,
                                         ChannelType type,
                                         Instant currentMessagedAt

) {
    public static ChannelPrivateResponseDto from(
            Channel channel,
            Instant currentMessagedAt
    ) {
        return ChannelPrivateResponseDto.builder()
                .channelId(channel.getId())
                .title(channel.getTitle())
                .description(channel.getDescription())
                .userIds(channel.getUserIds())
                .messageIds(channel.getMessageIds())
                .type(channel.getType())
                .currentMessagedAt(currentMessagedAt)
                .build();
    }
}