package com.sprint.mission.discodeit.integration.fixtures;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

public final class ChannelFixture {

    private ChannelFixture() {
    }

    public static Channel createPublicChannel(ChannelRepository channelRepository) {
        return channelRepository.save(defaultPublicChannel());
    }

    public static Channel createPublicChannel(String name, String description, ChannelRepository channelRepository) {
        return channelRepository.save(defaultPublicChannel(name, description));
    }

    public static Channel createPrivateChannel(ChannelRepository channelRepository) {
        return channelRepository.save(defaultPrivateChannel());
    }

    public static Channel defaultPrivateChannel() {
        return Channel.createPrivateChannel();
    }

    public static Channel defaultPublicChannel(String name, String description) {
        return Channel.createPublicChannel(name, description);
    }

    public static Channel defaultPublicChannel() {
        return Channel.createPublicChannel("title", "description");
    }

}
