package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(
        name = "read_statuses",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "channel_id"}
        )
)
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;
    @CreatedDate
    private Instant lastReadAt;

    @Builder
    public ReadStatus(User user, Channel channel) {
        if (channel.getType() != ChannelType.PRIVATE) {
            throw new IllegalArgumentException("public channel은 readStatus 생성 허용 되지 않습니다.");
        }
        this.user = user;
        this.channel = channel;
    }

    public boolean updateReadAt(Instant readAt) {

        if (readAt == null) {
            throw new IllegalArgumentException("값이 잘못되었습니다");
        }
        // 최초이면 무조건 세팅
        if (this.lastReadAt == null) {
            this.lastReadAt = readAt;
            return true;
        }
        // 이후엔 더 미래일 때만 변경
        if (readAt.isAfter(this.lastReadAt)) {
            this.lastReadAt = readAt;
            return true;
        }

        return false;
    }


}
