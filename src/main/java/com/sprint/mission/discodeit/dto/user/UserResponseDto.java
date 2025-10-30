package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.RoleType;
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

    public static UserResponseDto from(User user, UserStatus userStatus) {
        return UserResponseDto.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileId(user.getProfileId())
                .userStatus(userStatus)
                .build();
    }

}
