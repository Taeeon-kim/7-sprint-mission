package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "read_statuses", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "channel_id"})
})
public class ReadStatus extends BaseUpdatableEntity {

    @Column(name = "last_read_at", nullable = false)
    private Instant lastActiveAt; //마지막으로 읽은 시각

    //N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User user; //읽은 유저 정보

    // N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel; //채널 정보

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
