package com.sprint.mission.discodeit.service.factory;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelMinimumMembersNotMetException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateChannelCreator implements ChannelCreator {

    @Override
    public ChannelType supportedType() {
        return ChannelType.PRIVATE;
    }

    @Override
    public Channel create(ChannelCreateCommand command) {
        if (command.memberIds() == null || command.memberIds().isEmpty()) {
           throw new ChannelMinimumMembersNotMetException();
        }
        return Channel.createPrivateChannel();
    }
}
