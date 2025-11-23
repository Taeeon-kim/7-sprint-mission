package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

public record ChannelUpdateParams(
        String name,
        String description
) {
    public static ChannelUpdateParams from(ChannelUpdateRequestDto request) {
        return new ChannelUpdateParams(
                request.newName(),
                request.newDescription()
        );

    }
}
