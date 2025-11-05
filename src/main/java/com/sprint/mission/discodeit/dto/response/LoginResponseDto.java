package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.StatusType;
import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private final String userId;
    private final String userName;
    private final String userEmail;

    public static LoginResponseDto from(User user) {
        return LoginResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getEmail())
                .build();
    }
}
