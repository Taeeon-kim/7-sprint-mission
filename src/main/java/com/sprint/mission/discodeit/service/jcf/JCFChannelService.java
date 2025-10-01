package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public void createChannel(Channel channel) {
        channels.put(channel.getId(), channel);
        System.out.println("[Channel 생성] : " + channel);
    }

    @Override
    public Channel readChannel(UUID uuid) {
        return channels.get(uuid);
    }

    @Override
    public List<Channel> readAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void updateChannel(UUID uuid, String newName) {
        Channel ch = channels.get(uuid);
        if (ch!=null) ch.setChanName(newName);
    }

    @Override
    public void deleteChannel(UUID uuid) {
        Channel removed = channels.remove(uuid);
        System.out.println("[Channel 삭제] : " + removed);
    }
}
