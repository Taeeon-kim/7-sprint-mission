package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.StatusType;
import lombok.Getter;

import java.time.Instant;

@Getter
public class UserStatusUpdateRequestDto {
    private Instant lastActiveAt;
    private StatusType status;
}
