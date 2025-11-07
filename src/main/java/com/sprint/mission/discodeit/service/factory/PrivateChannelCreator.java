package com.sprint.mission.discodeit.service.factory;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PrivateChannelCreator implements ChannelCreator {
    private final UserReader userReader;

    @Override
    public ChannelType supportedType() {
        return ChannelType.PRIVATE;
    }

    @Override
    public Channel create(ChannelCreateCommand command) {
        userReader.findUserOrThrow(command.userId());
        if(command.memberIds() == null || command.memberIds().isEmpty()) {
            throw new IllegalArgumentException("memberIds required");
        }
        for (UUID userId : command.memberIds()){
            userReader.findUserOrThrow(userId);
        }
        Channel privateChannel = Channel.createPrivateChannel(command.userId());
        command.memberIds().forEach(privateChannel::addUserId);
        return privateChannel;
    }
}
