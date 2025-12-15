package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
public class ReadStatus extends BaseUpdatableEntity{

    private UUID userId; //읽은 유저 정보
    private UUID channelId; //채널 정보
    private Instant lastActiveAt; //마지막으로 읽은 시각

    //N:1
    private User user;

    // N:1
    private Channel channel;

    public ReadStatus(User user, Channel channel
    ) {
        super();
        this.user = user;
        this.channel = channel;
        this.lastActiveAt = Instant.now();
    }

    public void setUpdate(Instant newLastActiveAt) {
        if(newLastActiveAt != null && newLastActiveAt.isAfter(this.lastActiveAt)) {
            this.lastActiveAt = newLastActiveAt;
        }
    }
}
