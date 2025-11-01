package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageSendRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    // 모든 메세지 읽기(채널상관없이)
    List<Message> getAllMessages();

    // 특정 채널의 모든 메세지 읽기
    List<Message> getAllMessagesByChannelId(UUID channelId);

    // 단일 특정 메세지 읽기
    Message getMessageById(UUID messageId);

    // 특정 메세지(uuid 사용)생성 후 보내기(채널로)
    UUID sendMessageToChannel(MessageSendRequestDto request); // TODO: 추후 컨트롤러 계층생성시 파라미터를 DTO로 변경(파라미터가 길어질시)
    // 메세지 보내기(개인) -> 결국 개인도 하나의 또다른 채널에서 1:1로만 한다고 가정하면될듯, 그럼 receiver가 필요한가? 개인이면 필요, 그룹이면 Null?

    // 특정 메세지(uuid 사용) 수정하기
    void updateMessage(UUID messageId, String content);

    // 특정 메세지(uuid 사용) 삭제하기
    void deleteMessage(UUID messageId);

//     List<Message> getAllMessagesByIds(List<UUID> messageIds);


}
