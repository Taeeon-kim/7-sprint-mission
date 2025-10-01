package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BasicEntity {
    private String content;
    private final UUID senderId;
    //    private final UUID receiverId; // 일단 dm 없으므로 제외
    private final UUID channelId; // TODO: Channel과 연관관계 체크할것

    public Message(String content, UUID senderId, UUID channelId) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content is valid");
        }
        if (senderId == null) {
            throw new IllegalArgumentException("SenderId is null");
        }

        if (channelId == null) {
            throw new IllegalArgumentException("ChannelId is null");
        }
        this.content = content.trim();
        this.senderId = senderId;
//        this.receiverId = receiverId;
        this.channelId = channelId;
    }


    public String getContent() {
        return content;
    }

    public UUID getSenderId() {
        return senderId;
    }

//    public UUID getReceiverId() {
//        return receiverId;
//    }

    public UUID getChannelId() {
        return channelId;
    }

    public boolean updateContent(String content) {
        if (content == null) return false;
        String trimmedContent = content.trim();
        if (trimmedContent.isBlank() || trimmedContent.equals(this.content)) return false;
        this.content = trimmedContent;
        return true;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id='" + getId() + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", content='" + content + '\'' +
                ", senderId=" + senderId +
//                ", receiverId=" + receiverId +
                ", channelId=" + channelId +
                "}\n";
    }
}
