package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelNotJoinedException extends ChannelException {
    public ChannelNotJoinedException(UUID userId, UUID channelId) {
        super(ErrorCode.CHANNEL_NOT_JOINED, Map.of("userId", userId, "channelId", channelId));
    }
}
