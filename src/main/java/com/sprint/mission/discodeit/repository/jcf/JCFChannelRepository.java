package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channels = new HashMap<>();

    private JCFChannelRepository() { }

    private static final JCFChannelRepository INSTANCE = new JCFChannelRepository();

    public static JCFChannelRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Channel channel) {
        channels.put(channel.getUuid(), channel);
    }

    @Override
    public Optional<Channel> findByChannel(UUID uuid) {
        return Optional.ofNullable(channels.get(uuid));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void deleteChannel(UUID uuid) {
        channels.remove(uuid);
    }

//    // 채널명 수정
//    @Override
//    public void updateChannel(UUID uuid, String newChannel) {
//        Channel ch = channels.get(uuid);
//        if(ch != null) ch.setChanName(newChannel);
//    }
}
