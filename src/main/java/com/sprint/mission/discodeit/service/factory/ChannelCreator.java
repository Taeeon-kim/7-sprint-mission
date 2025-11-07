package com.sprint.mission.discodeit.service.factory;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;

public interface ChannelCreator {

    ChannelType supportedType();
    Channel create(ChannelCreateCommand command);
}
