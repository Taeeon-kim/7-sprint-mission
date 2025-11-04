package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    void save(Message message); // 메시지 저장

    Message findByMessage(UUID uuid); // 메시지 조회

    List<Message> findUserAll(User user); // 유저관련 메시지 전체조회

    List<Message> findChannelAll(Channel channel); // 채널관련 메시지 전체조회

    void deleteMessage(UUID uuid); // 메시지 삭제

}