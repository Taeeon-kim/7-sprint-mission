package com.sprint.mission.discodeit.service.factory;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PrivateChannelCreator implements ChannelCreator {
    private final UserReader userReader;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelType supportedType() {
        return ChannelType.PRIVATE;
    }

    @Override
    public Channel create(ChannelCreateCommand command) {
        if (command.memberIds() == null || command.memberIds().isEmpty()) {
            throw new IllegalArgumentException("memberIds required");
        }
        return Channel.createPrivateChannel();
    }
}
