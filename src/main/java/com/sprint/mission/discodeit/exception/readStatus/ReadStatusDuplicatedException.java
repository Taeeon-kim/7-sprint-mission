package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ReadStatusDuplicatedException extends ReadStatusException {

    public ReadStatusDuplicatedException(UUID userId, UUID channelId) {
        super(ErrorCode.READ_STATUS_DUPLICATED, Map.of("userId", userId, "channelId", channelId));
    }

}
