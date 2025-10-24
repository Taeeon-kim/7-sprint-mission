package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void createMessage(Message message); //메시지 전송

    Message getMessage(UUID uuid); //메시지 조회

    List<Message> getUserAllMessage(User users); //유저 관련 전체 메시지 조회

    List<Message> getChannelAllMessage(Channel channels); // 채널 관련 전체 메시지 조회

    void updateMessage(UUID uuid, String newMessage); //수정

    void deleteMessage(UUID uuid); //삭제

}
