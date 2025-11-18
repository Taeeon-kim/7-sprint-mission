package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    
    void save(Channel channel); // 채널 저장
    
    Optional<Channel> findByChannel(UUID uuid); // 채널 보기

    Optional<Channel> findByChannelName(String channelName);

    List<Channel> findAll(); // 채널 전체 보기

    void deleteChannel(UUID uuid); // 채널 삭제
}