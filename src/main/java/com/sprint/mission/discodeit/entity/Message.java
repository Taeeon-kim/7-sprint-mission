package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter @ToString
public class Message extends BaseEntity {

    private final UUID sendUser; //보내는 사람
    private final UUID receiverUser; //받는 사람
    private String inputMsg; //메시지 내용

    public Message(UUID sendUser, UUID receiverUser, String inputMsg) {
        super();
        this.sendUser = sendUser;
        this.receiverUser = receiverUser;
        this.inputMsg = inputMsg;
    }

    public void setInputMsg(String inputMsg) {
        if(inputMsg != null || !inputMsg.equals(this.inputMsg)) {
            this.inputMsg = inputMsg;
            setUpdatedAt(Instant.now()); //변경 시 갱신
        }
    }
}

