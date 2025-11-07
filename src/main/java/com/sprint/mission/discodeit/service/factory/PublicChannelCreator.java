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
public class PublicChannelCreator implements ChannelCreator {

    private final UserReader userReader;

    @Override
    public ChannelType supportedType() {
        return ChannelType.PUBLIC;
    }

    @Override
    public Channel create(ChannelCreateCommand command) {
        userReader.findUserOrThrow(command.userId());
        if (command.title() == null || command.title().isBlank() && command.description() == null) {
            throw new IllegalArgumentException("title/description required");
        }
        return Channel.createPublicChannel(command.userId(), command.title(), command.description());
    }
}
