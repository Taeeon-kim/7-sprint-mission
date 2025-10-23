package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

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

    public UUID getSendUser() {
        return sendUser;
    }

    public UUID getReceiverUser() {
        return receiverUser;
    }

    public String getInputMsg() {
        return inputMsg;
    }

    public void setInputMsg(String inputMsg) {
        if(inputMsg != null || !inputMsg.equals(this.inputMsg)) {
            this.inputMsg = inputMsg;
            setUpdatedAt(System.currentTimeMillis()); //변경 시 갱신
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "sendUser='" + sendUser + '\'' +
                ", receiverUser='" + receiverUser + '\'' +
                ", inputMsg='" + inputMsg + '\'' +
                ", createAt='" + getCreateAt() + '\'' +
                ", updateAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}

