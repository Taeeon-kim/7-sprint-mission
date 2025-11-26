package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;

import com.sprint.mission.discodeit.entity.type.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        String name,
        String description,
        List<UserResponseDto> participants,
        ChannelType type,
        Instant lastMessageAt
) {
}
