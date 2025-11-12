package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

public record ChannelUpdateParams(
        String title,
        String description
) {
    public static ChannelUpdateParams from(ChannelUpdateRequestDto request) {
        return new ChannelUpdateParams(
                request.title(),
                request.description()
        );

    }
}
