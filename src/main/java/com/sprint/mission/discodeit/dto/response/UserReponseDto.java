package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.StatusType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserReponseDto {
    private String userId;
    private String userName;
    private String userEmail;
    private UUID profileImagePath;

    public static UserReponseDto from(User user) {
        return UserReponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getEmail())
                .profileImagePath(user.getProfileImageId())
                .build();
    }
}
