package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
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
    public void save(Channel channel) {

    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.empty();
    }

    @Override
    public Map<UUID, Channel> findAllMap() {
        return Map.of();
    }

    @Override
    public List<Channel> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(UUID channelId) {

    }
}
