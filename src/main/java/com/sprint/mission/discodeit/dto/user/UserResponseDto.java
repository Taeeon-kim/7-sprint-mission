package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import com.sprint.mission.discodeit.entity.type.RoleType;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String nickname,
        String email,
        RoleType role,
        UUID profileId,
        UserActiveStatus isOnline,
        Instant createdAt,
        Instant updatedAt
) {

    public static UserResponseDto from(User user, UserActiveStatus userStatus) {
        return new UserResponseDto(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getRole(),
                user.getProfileId(),
                userStatus,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
