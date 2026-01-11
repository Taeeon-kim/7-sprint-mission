package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class ReadStatusCreateNotAllowedException extends ReadStatusException {
    public ReadStatusCreateNotAllowedException(ChannelType channelType) {
        super(ErrorCode.READ_STATUS_CREATE_NOT_ALLOWED, Map.of("channelType", channelType));
    }
}
