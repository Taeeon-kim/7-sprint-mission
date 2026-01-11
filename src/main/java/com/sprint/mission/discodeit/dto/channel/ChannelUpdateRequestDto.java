package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

@Builder
public record ChannelUpdateRequestDto(
        String newName,
        String newDescription
) {

}
