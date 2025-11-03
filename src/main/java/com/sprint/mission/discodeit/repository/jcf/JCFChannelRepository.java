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

    // 채널 생성
    @Override
    public void save(Channel channel) {
        channels.put(channel.getUuid(), channel);
    }

    // 채널 1개 조회
    @Override
    public Channel findByChannel(UUID uuid) {
        return channels.get(uuid);
    }

    // 채널 전체 조회
    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    // 채널명 수정
    @Override
    public void updateChannel(UUID uuid, String newChannel) {
        Channel ch = channels.get(uuid);
        if(ch != null) ch.setChanName(newChannel);
    }

    // 채널 삭제
    @Override
    public void deleteChannel(UUID uuid) {
        channels.remove(uuid);
    }
}
