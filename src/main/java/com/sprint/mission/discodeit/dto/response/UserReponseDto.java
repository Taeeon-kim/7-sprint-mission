package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.StatusType;
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
    private String profileImagePath;
}
