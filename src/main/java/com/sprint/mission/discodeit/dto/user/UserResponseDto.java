package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.RoleType;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResponseDto {
    private UUID id;
    private String nickname;
    private String email;
    private RoleType role;
    private String phoneNumber;
    private UUID profileId;
    UserStatus userStatus;
}
