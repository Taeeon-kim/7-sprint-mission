package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
    public static UserDto from(User user, UserActiveStatus userStatus) {
        return new UserDto(user.getId(), 
                user.getCreatedAt(),
                user.getUpdatedAt(), 
                user.getNickname(),
                user.getEmail(),
                user.getProfileId(),
                userStatus == UserActiveStatus.ONLINE
        );


    }
}