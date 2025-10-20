package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    void createChannel(Channel channel); //채널 생성

    Channel readChannel(UUID uuid); //채널 내용 보기

    List<Channel> readAllChannels();

    void updateChannel(UUID uuid, String newName); //채널 수정

    void deleteChannel(UUID uuid); //채널 삭제


}
