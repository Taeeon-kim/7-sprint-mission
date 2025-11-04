package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.type.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelCreateCommand(
        UUID userId,
        String title,
        String description,
        ChannelType type,
        List<UUID> memberIds
) {

    public static ChannelCreateCommand from(ChannelCreateRequestDto requestDto) {
        return new ChannelCreateCommand(
                requestDto.userId(),
                requestDto.title(),
                requestDto.description(),
                requestDto.type(),
                requestDto.memberIds() == null ? List.of() : requestDto.memberIds()
        );
    }
}
