package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ReadStatus extends BaseUpdatableEntity {
    UUID userId;
    UUID channelId;
    Instant readAt;

    public void markAsRead() {
        this.readAt = Instant.now();
    }

    public boolean updateReadAt(Instant readAt) {
        boolean isUpdated = false;
        if (readAt == null) {
            throw new IllegalArgumentException("값이 잘못되었습니다");
        }
        if (this.readAt != null && readAt.isAfter(this.readAt) && !this.readAt.equals(readAt)) {
            this.readAt = readAt;
            isUpdated = true;
        }

        return isUpdated;
    }


}
