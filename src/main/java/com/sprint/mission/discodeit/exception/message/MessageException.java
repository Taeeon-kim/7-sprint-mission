package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public abstract class MessageException extends DiscodeitException {
    protected MessageException(ErrorCode errorCode, UUID userId, UUID channelId, ChannelType channelType) {
        super(errorCode, Map.of("userId", userId, "channelId", channelId, "channelType", channelType));
    }
}
