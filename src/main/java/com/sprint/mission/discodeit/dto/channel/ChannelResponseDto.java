package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        String name,
        String description,
        List<UserResponseDto> participants,
        ChannelType type,
        Instant lastMessagedAt
) {
    public static ChannelResponseDto from(
            Channel channel,
            List<User> participants,
            Instant lastMessagedAt
    ) {
        List<UserResponseDto> userResponseDtos = participants.stream()
                .map(user -> UserResponseDto.from(user))
                .toList();
        return new ChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                userResponseDtos,
                channel.getType(),
                lastMessagedAt

        );
    }
}
