package com.sprint.mission.discodeit.service.factory;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelUnsupportedTypeException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChannelFactory {
    private final Map<ChannelType, ChannelCreator> creators;

    public ChannelFactory(List<ChannelCreator> creators) {
        this.creators = creators.stream()
                .collect(Collectors.toMap(ChannelCreator::supportedType, creator -> creator));
    }

    public Channel create(ChannelCreateCommand command) {
        ChannelCreator creator = creators.get(command.type());
        if (creator == null) {
            throw new ChannelUnsupportedTypeException(command.type());
        }

        return creator.create(command);
    }
}
