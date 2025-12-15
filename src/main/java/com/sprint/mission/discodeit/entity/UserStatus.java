package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

    private Instant lastActiveAt; //마지막 접속시간
    private StatusType status; //Online, Offline 표시

    // 1:1
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
