package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserReponseDto {
    private UUID userUuid;
    private String username;
    private String email;
    private String nickname;
    private String profileImagePath;
    private String profileContentType;
    private boolean online;
    private Instant createAt;
}
