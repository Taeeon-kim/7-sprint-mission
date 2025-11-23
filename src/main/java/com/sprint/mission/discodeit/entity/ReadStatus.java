package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
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
