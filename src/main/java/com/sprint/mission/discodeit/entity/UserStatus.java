package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
 * 사용자의 온라인 상태를 확인하기 위해 활용
 * 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드 정의
 * - 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속중인 유저로 간주함
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus{

    private UUID uuid; //UserStatus의 UUID
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId; // 유저의 UUID
    private Instant lastActiveAt; //마지막 접속시간
    private StatusType status; //Online, Offline 표시


    public UserStatus(UUID userId) {
        this.uuid = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
        this.lastActiveAt = null;
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
        return Duration.between(createdAt, Instant.now()).toMinutes() <= 5;
    }

}
