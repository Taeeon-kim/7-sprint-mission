package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter @ToString
public class Message extends BaseEntity {

    private final UUID senderId; // 보낸 사람
    private final UUID channelId; // 작성한 채널
    private String inputMsg; // 내용

    public Message(UUID senderId, UUID channelId, String inputMsg) {
        super();
        this.senderId = senderId;
        this.channelId = channelId;
        this.inputMsg = inputMsg;
    }

    public void setInputMsg(String inputMsg) {
        if(inputMsg != null || !inputMsg.equals(this.inputMsg)) {
            this.inputMsg = inputMsg;
            setUpdatedAt(Instant.now()); //변경 시 갱신
        }
    }
}

