package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelModificationNotAllowedException extends ChannelException {
    public ChannelModificationNotAllowedException(UUID channelId, ChannelType channelType) {
        super(ErrorCode.CHANNEL_MODIFICATION_NOT_ALLOWED, Map.of(
                "channelId", channelId,
                "channelType", channelType)
        );
    }
}
