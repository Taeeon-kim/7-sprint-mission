package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {
    @OneToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt;

    public void markAsActive() {
        this.lastActiveAt = Instant.now();
    }

    public UserStatus(User user) {
        this.user = user;
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
