package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author; //TODO: Channel과 연관관계 체크할것

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel; // TODO: Channel과 연관관계 체크할것

//    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MessageAttachment> attachments; // TODO: 중간테이블을 직접 만든 MessageAttachment로할지 피드백후 결정

    @OneToMany // N:1 테이블
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),         // 이 엔티티의 FK
            inverseJoinColumns = @JoinColumn(name = "attachment_id") // BinaryContent FK
    )
    private List<BinaryContent> attachments = new ArrayList<>();

    @Builder
    public Message(String content, User author, Channel channel, List<BinaryContent> attachments) {

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content is invalid");
        }
        if (author == null) {
            throw new IllegalArgumentException("author is null");
        }

        if (channel == null) {
            throw new IllegalArgumentException("channel is null");
        }
        this.content = content.trim();
        this.author = author;
        this.channel = channel;
        if (attachments != null) {
            this.attachments.addAll(attachments);
        }
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
                "}\n";
    }
}
