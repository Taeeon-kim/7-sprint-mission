package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserStatus extends BasicEntity {
    private final UUID userId;
    private Instant lastActiveAt;


    public void markAsActive() {
        this.lastActiveAt = Instant.now();
        setUpdatedAt(Instant.now());
    }

    public UserActiveStatus getUserStatus() {
        Instant fiveMinutesAgo = Instant.now().minusSeconds(300);
        boolean recentlyActive = lastActiveAt.isAfter(fiveMinutesAgo);

        return recentlyActive ? UserActiveStatus.ONLINE : UserActiveStatus.OFFLINE;
    }

}
