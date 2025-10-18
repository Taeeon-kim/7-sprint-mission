package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    //저장소
    private final ChannelRepository repository = JCFChannelRepository.getInstance();

    private JCFChannelService() { }

    private static final JCFChannelService INSTANCE = new JCFChannelService();

    public static JCFChannelService getInstance(){
        return INSTANCE;
    }

    @Override
    public void createChannel(Channel channel) {
        repository.save(channel);
        System.out.println("[Channel 생성] : " + channel);
    }

    @Override
    public Channel readChannel(UUID uuid) {
        return repository.findByChannel(uuid);
    }

    @Override
    public List<Channel> readAllChannels() {
        return repository.findAll();
    }

    @Override
    public void updateChannel(UUID uuid, String newName) {
        repository.updateChannel(uuid, newName);
        System.out.println("[채널명 수정] : " + newName);
    }

    @Override
    public void deleteChannel(UUID uuid) {
        repository.deleteChannel(uuid);
        System.out.println("[채널 삭제]");
    }
}
