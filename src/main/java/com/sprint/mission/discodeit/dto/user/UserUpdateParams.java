package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserUpdateParams(
        String nickname,
        String email,
        String password,
        UUID profileId
) {

    public static UserUpdateParams from(UserUpdateCommand updateCommand, UUID profileBinaryId) {
        return new UserUpdateParams(
                updateCommand.username(),
                updateCommand.email(),
                updateCommand.password(),
                profileBinaryId
        );

    }
}
