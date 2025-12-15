package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt; //마지막 접속시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusType status; //Online, Offline 표시

    // 1:1
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public UserStatus(User user) {
        super();
        this.user = user;
        this.lastActiveAt = Instant.now();
        this.status = StatusType.ONLINE;
    }

    private void updateStatus() {
        this.status = isOnline() ? StatusType.ONLINE : StatusType.OFFLINE;
    }

    public StatusType getStatus() {
        updateStatus();
        return this.status;
    }

    public void updateLastActiveAt() {
        this.lastActiveAt = Instant.now();
        updateStatus(); // 자동 ONLINE
    }

    public boolean isOnline() {
        return Duration.between(lastActiveAt, Instant.now()).toMinutes() <= 5;
    }

}
