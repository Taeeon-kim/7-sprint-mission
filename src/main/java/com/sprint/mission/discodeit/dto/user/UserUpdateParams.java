package com.sprint.mission.discodeit.dto.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateParams {
    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;
    private UUID profileId;

    public static UserUpdateParams from(UserUpdateRequestDto dto) {
        return UserUpdateParams.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .profileId(dto.getProfileId())
                .build();
    }
}
