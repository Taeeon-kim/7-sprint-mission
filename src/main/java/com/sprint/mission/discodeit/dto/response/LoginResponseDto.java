package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
//    private final String id;
    private final String username; //
    private final String email; //
    private final String password;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final UUID id;
    private final UUID profileId;

    public static LoginResponseDto from(User user) {
        return LoginResponseDto.builder()
//                .userId(user.getUserId())
                .id(user.getUuid())
                .username(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .createdAt(user.getCreateAt())
                .updatedAt(user.getUpdatedAt())
                .profileId(user.getProfileImageId())
//                .userEmail(user.getEmail())
                .build();
    }
}
