package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import com.sprint.mission.discodeit.entity.type.RoleType;
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
    UserActiveStatus isOnline;

    public static UserResponseDto from(User user, UserActiveStatus userStatus) {
        return UserResponseDto.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileId(user.getProfileId())
                .isOnline(userStatus)
                .build();
    }

}
