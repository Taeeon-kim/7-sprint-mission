package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

@Builder
public record ChannelUpdateRequestDto(
        String title,
        String description
) {

}
