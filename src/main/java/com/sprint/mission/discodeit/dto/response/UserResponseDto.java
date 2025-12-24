package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private UUID id;
    private String userName;
    private String email;
    private String status;
    private BinaryContentResponseDto profile;

    public static UserResponseDto from(User user, UserStatus userStatus, BinaryContent binaryContent) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
//                .status(user.getUserStatus() != null
//                        ? user.getUserStatus().getStatus().name() : null)
                .profile(user.getProfile() != null
                        ? BinaryContentResponseDto.from(user.getProfile()) : null)
                .build();
    }
}
