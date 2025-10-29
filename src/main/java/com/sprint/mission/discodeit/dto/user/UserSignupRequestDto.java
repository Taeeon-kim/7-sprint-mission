package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {
    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;
    private UUID profileId;
}
