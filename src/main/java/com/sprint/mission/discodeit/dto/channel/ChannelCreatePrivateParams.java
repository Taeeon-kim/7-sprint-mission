package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ChannelCreatePrivateParams(
        List<UUID> memberIds
) {
    public static ChannelCreatePrivateParams from(ChannelCreateCommand requestDto) {
        return ChannelCreatePrivateParams.builder()
                .memberIds(requestDto.memberIds())
                .build();
    }
}
