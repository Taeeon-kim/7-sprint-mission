package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChannelMinimumMembersNotMetException extends ChannelException {

    public ChannelMinimumMembersNotMetException(int participantsNumber, int required, List<UUID> participantIds) {
        super(ErrorCode.CHANNEL_MINIMUM_MEMBERS_NOT_MET, Map.of(
                "participantsNumber", participantsNumber,
                "required", required,
                "participantIds", participantIds
        ));
    }
}
