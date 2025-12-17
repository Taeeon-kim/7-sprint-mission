package com.sprint.mission.discodeit.service.factory;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublicChannelCreator implements ChannelCreator {

    @Override
    public ChannelType supportedType() {
        return ChannelType.PUBLIC;
    }

    @Override
    public Channel create(ChannelCreateCommand command) {
        if ((command.title() == null || command.title().isBlank()) && command.description() == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }
        return Channel.createPublicChannel(command.title(), command.description());
    }
}
