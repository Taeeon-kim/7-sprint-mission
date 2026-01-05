package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelUnsupportedTypeException extends ChannelException {

    public ChannelUnsupportedTypeException(ChannelType channelType) {
        super(ErrorCode.UNSUPPORTED_CHANNEL_TYPE, Map.of("channelType", channelType));
    }

    public ChannelUnsupportedTypeException(UUID channelId, ChannelType channelType) {
        super(ErrorCode.UNSUPPORTED_CHANNEL_TYPE, Map.of("channelId", channelId, "channelType", channelType));
    }
}
