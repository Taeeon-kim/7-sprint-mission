package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelAccessDeniedException extends ChannelException {
    public ChannelAccessDeniedException(UUID channelId, UUID userId) {
        super(
                ErrorCode.CHANNEL_ACCESS_DENIED,
                Map.of(
                        "channelId", channelId,
                        "userId", userId
                )
        );
    }
}
