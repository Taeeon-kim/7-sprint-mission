package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.StatusType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class UserStatusResponseDto {
    private UUID uuid;
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastActiveAt;
    private StatusType status;

    public static UserStatusResponseDto from(UserStatus userStatus){
        return UserStatusResponseDto.builder()
                .uuid(userStatus.getUserId())
                .userId(userStatus.getUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .lastActiveAt(Instant.now())
                .build();
    }
}
