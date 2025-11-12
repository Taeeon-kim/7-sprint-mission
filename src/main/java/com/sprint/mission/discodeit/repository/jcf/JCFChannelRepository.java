package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true)
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new ConcurrentHashMap<>();

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
