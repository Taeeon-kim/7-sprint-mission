package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ReadStatus extends BasicEntity {
    UUID userId;
    UUID channelId;
    Instant readAt;

    public void markAsRead() {
        this.readAt = Instant.now();
        setUpdatedAt(Instant.now());
    }


}
