package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class ChannelInvalidParticipantsException extends DiscodeitException {
    public ChannelInvalidParticipantsException(
            int requested,
            int found
    ) {
        super(ErrorCode.CHANNEL_INVALID_PARTICIPANTS, Map.of(
                "requested", requested,
                "found", found
        ));
    }
}
