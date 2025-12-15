package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelAlreadyJoinedException extends ChannelException {
    public ChannelAlreadyJoinedException(UUID userId, UUID channelId) {
        super(ErrorCode.CHANNEL_ALREADY_JOINED, Map.of(
                "userId", userId,
                "channelId", channelId)
        );
    }
}
