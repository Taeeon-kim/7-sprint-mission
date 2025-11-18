package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message); // 메시지 저장

    Optional<Message> findByMessage(UUID uuid); // 메시지 조회

    List<Message> findAll(); //메시지 전체 조회

    List<Message> findAllByChannelId(Channel channel); // 채널관련 메시지 전체조회

    List<Message> findByChannelId(UUID channelId); // 채널에 속한 메시지의 파일 확인

    Optional<Instant> findLastByChannel(UUID channelId); // 가장 마지막에 온 메시지

    void deleteMessage(UUID uuid); // 메시지 삭제

    void deleteAllByChannelId(UUID channelId); // 특정 채널의 모든 메시지 삭제
}