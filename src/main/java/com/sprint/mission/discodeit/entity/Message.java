package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BasicEntity{
    private String content;
    private final UUID senderId;
    private final UUID receiverId;
    private final UUID channelId; // TODO: Channel과 연관관계 체크할것

    public Message(String content, UUID senderId, UUID receiverId, UUID channelId) {
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.channelId = channelId;
    }


    public String getContent() {
        return content;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public UUID getChannelId() {
        return channelId;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id='" + getId() + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                "content='" + content + '\'' +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", channelId=" + channelId +
                '}';
    }
}
