package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    // 채널 저장 : save
    
//    Optional<Channel> findById(Channel channel); // 채널 보기

//    Optional<Channel> findByChannelName(String channelName);

    // 채널 전체 보기 : findAll

    // 채널 삭제 : delete
}