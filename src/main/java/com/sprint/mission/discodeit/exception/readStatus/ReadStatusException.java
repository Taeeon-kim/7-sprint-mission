package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public abstract class ReadStatusException extends DiscodeitException {

    protected ReadStatusException(ErrorCode errorCode, UUID readStatusId) {
        super(errorCode, Map.of("readStatusId", readStatusId));
    }

    protected ReadStatusException(ErrorCode errorCode, UUID userId, UUID channelId) {
        super(errorCode, Map.of("userId", userId, "channelId", channelId));
    }
}
