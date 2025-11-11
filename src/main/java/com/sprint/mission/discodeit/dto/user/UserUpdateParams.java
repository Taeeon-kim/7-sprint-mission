package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserUpdateParams(
        String nickname,
        String email,
        String password,
        UUID profileId
) {

    public static UserUpdateParams from(UserUpdateRequestDto dto) {
        return new UserUpdateParams(
                dto.getNickname(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getProfileId()
        );

    }
}
