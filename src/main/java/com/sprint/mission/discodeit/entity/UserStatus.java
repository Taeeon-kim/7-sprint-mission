package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BasicEntity {
    private final UUID userId;
    private Instant lastActiveAt;


    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public UserActiveStatus getUserStatus() {
        Instant fiveMinutesAgo = Instant.now().minusSeconds(300);
        boolean recentlyActive = lastActiveAt.isAfter(fiveMinutesAgo);

        return recentlyActive ? UserActiveStatus.ONLINE : UserActiveStatus.OFFLINE;
    }

}
