package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseUpdatableEntity {
    private final UUID userId;
    private Instant lastActiveAt;

    public void markAsActive() {
        this.lastActiveAt = Instant.now();
    }

    public UserStatus(UUID userId) {
        this.userId = userId;
        this.lastActiveAt = Instant.now();
    }

    public UserActiveStatus getUserStatus() {
        Instant fiveMinutesAgo = Instant.now().minusSeconds(300);
        boolean recentlyActive = lastActiveAt.isAfter(fiveMinutesAgo);

        return recentlyActive ? UserActiveStatus.ONLINE : UserActiveStatus.OFFLINE;
    }

    public boolean updateLastActiveAt(Instant lastActiveAt) {
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            if (lastActiveAt.isAfter(Instant.now())) {
                throw new IllegalArgumentException("해당 시간으로 변경할 수 없습니다.");
            }
            this.lastActiveAt = lastActiveAt;
            return true;
        }
        return false;
    }

}
