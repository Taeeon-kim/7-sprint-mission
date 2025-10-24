package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

/**
 * 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
 * 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
 */
@Getter @ToString
public class ReadStatus {

    private final UUID uuid;
    private final UUID userId; // 읽은 유저
    private final UUID channelId; // 채널 정보
    private Instant lastReadAt; //마지막으로 읽은 시각

    public ReadStatus(UUID uuid, UUID userId, UUID channelId, Instant lastReadAt) {
        this.uuid = uuid;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastReadAt(Instant newReadAt) {
        this.lastReadAt = newReadAt;
    }

}
