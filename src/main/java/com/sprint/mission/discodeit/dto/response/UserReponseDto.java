package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserReponseDto {
    private String username;
    private String email;
    private String nickname;
    private String profileImagePath;
    private UserStatus status;
}
