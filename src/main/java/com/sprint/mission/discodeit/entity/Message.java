package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter

public class Message extends BaseUpdatableEntity {
    private String content;
    private final UUID senderId;
    //    private final UUID receiverId; // 일단 dm 없으므로 제외
    private final UUID channelId; // TODO: Channel과 연관관계 체크할것
    private List<UUID> attachmentIds;

    @Builder
    public Message(String content, UUID senderId, UUID channelId, List<UUID> attachmentIds) {

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content is invalid");
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
        this.attachmentIds = (attachmentIds == null) ? new ArrayList<>() : attachmentIds;
    }


    public boolean updateContent(String content) {
        if (content == null) return false;
        String trimmedContent = content.trim();
        if (trimmedContent.isEmpty() || trimmedContent.equals(this.content)) return false;
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
