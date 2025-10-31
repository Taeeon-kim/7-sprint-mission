package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

@Builder
public record ChannelUpdateParams(
        String title,
        String description
) {
    public static ChannelUpdateParams from(ChannelUpdateRequestDto request) {
        return ChannelUpdateParams.builder()
                .title(request.title())
                .description(request.description())
                .build();
    }
}
