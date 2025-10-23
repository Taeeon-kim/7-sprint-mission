package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {

    //의존성 주입
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository){
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(Channel channel) {
        channelRepository.save(channel);
        System.out.println("[Channel 생성] : " + channel);
    }

    @Override
    public Channel readChannel(UUID uuid) {
        return channelRepository.findByChannel(uuid);
    }

    @Override
    public List<Channel> readAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID uuid, String newName) {
        channelRepository.updateChannel(uuid, newName);
    }

    @Override
    public void deleteChannel(UUID uuid) {
        channelRepository.deleteChannel(uuid);
        System.out.println("[채널 삭제]");
    }

}
