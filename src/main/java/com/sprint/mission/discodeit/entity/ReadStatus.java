package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

/**
 * 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
 * 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
 */
@Getter
@ToString
public class ReadStatus {

    private UUID uuid; //고유 uuid
    private Instant createAt;
    private Instant updateAt;

    private UUID userId; //읽은 유저 정보
    private UUID channelId; //채널 정보
    private Instant lastActiveAt; //마지막으로 읽은 시각

    public ReadStatus(UUID userId, UUID channelId) {
        this.uuid = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastActiveAt = Instant.now();
    }

    public void setUpdate(Instant newLastActiveAt) {
        if(newLastActiveAt != null && newLastActiveAt.isAfter(this.lastActiveAt)) {
            this.lastActiveAt = newLastActiveAt;
        }
    }
}
