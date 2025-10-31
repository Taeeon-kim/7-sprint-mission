package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository(Map<UUID, Channel> data) {
        this.data = data;
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId));
    }

    @Override
    public Map<UUID, Channel> findAllMap() {
        return data;
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        List<Channel> allChannels = findAll();
        return allChannels.stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC || channel.isMember(userId))
                .toList();
    }

    @Override
    public List<Channel> findAll() {
        return data.values()
                .stream()
                .toList();
    }

    @Override
    public boolean deleteById(UUID channelId) {
        return data.remove(channelId) != null;
    }
}
