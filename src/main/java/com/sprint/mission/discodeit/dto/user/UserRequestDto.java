package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;
    private UUID profileId;

}
