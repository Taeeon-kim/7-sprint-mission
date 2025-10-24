package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BasicEntity {
    UUID userId;
    UUID channelId;
    Instant readAt;

}
