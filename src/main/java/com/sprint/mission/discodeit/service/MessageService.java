package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 모든 메세지 읽기(채널상관없이)
    // 특정 메세지(uuid 사용)생성 후 보내기(채널로)
    public void sendMessageToChannel(UUID channelId, UUID senderId, String content);
    // 메세지 보내기(개인) -> 결국 개인도 하나의 또다른 채널에서 1:1로만 한다고 가정하면될듯, 그럼 receiver가 필요한가? 개인이면 필요, 그룹이면 Null?
    // 특정 메세지(uuid 사용) 수정하기
    // 특정 메세지(uuid 사용) 삭제하기

//    public List<Message> getAllMessagesByIds(List<UUID> messageIds);
    public List<Message> getAllMessagesOfChannel(UUID channelId);


}
