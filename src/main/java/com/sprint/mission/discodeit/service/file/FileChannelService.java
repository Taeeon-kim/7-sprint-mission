package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {

    //의존성 주입
    private final ChannelRepository channelRepository = FileChannelRepository.getInstance();

    // 싱글톤
    private static final FileChannelService INSTANCE = new FileChannelService();

    private FileChannelService(){

    }

    public static FileChannelService getInstance(){
        return INSTANCE;
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
