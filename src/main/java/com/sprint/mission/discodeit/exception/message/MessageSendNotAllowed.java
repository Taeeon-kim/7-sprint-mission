package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class MessageSendNotAllowed extends MessageException{
    public MessageSendNotAllowed(UUID userId, UUID channelId, ChannelType channelType) {
        super(ErrorCode.MESSAGE_SEND_NOT_ALLOWED, userId, channelId, channelType);
    }
}
