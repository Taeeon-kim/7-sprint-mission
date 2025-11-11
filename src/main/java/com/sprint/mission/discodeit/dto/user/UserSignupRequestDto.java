package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequestDto {
    private String username;
    private String email;
    private String password;
//    private String phoneNumber;
//    private UUID profileId;
}
