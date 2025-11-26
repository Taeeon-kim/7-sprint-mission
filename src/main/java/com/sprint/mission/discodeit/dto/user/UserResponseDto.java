package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import com.sprint.mission.discodeit.entity.type.RoleType;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        BinaryContentResponseDto profile,
        Boolean online,
        Instant createdAt,
        Instant updatedAt
) {

    public static UserResponseDto from(User user) {

        BinaryContentResponseDto binaryContentResponseDto = Optional.ofNullable(user.getProfile()) // TODO: lazy가 나을지 eager가 나을지 혹은 feth join?
                .map(BinaryContentResponseDto::from)
                .orElse(null);


        UserActiveStatus userActiveStatus = Optional.ofNullable(user.getUserStatus()) // TODO: lazy가 나을지 eager가 나을지 혹은 feth join?
                .map(UserStatus::getUserStatus)
                .orElse(UserActiveStatus.OFFLINE);

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentResponseDto,
                userActiveStatus == UserActiveStatus.ONLINE,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
