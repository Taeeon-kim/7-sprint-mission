package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record UserUpdateParams(
        String nickname,
        String email,
        String password,
        BinaryContent profile
) {

    public static UserUpdateParams from(UserUpdateCommand updateCommand, BinaryContent binaryContent) {
        return new UserUpdateParams(
                updateCommand.username(),
                updateCommand.email(),
                updateCommand.password(),
                binaryContent
        );

    }
}
