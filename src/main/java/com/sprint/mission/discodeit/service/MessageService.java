package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void createMsg(Message msg); //메시지 전송

    Message getMsg(UUID uuid); //메시지 조회

    List<Message> getAllMsg(User userId); //유저 관련 전체 메시지 조회

    void updateMsg(UUID uuid, String newMsg); //수정

    void deleteMsg(UUID uuid); //삭제

}
