package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Message extends BaseUpdatableEntity {

//    private UUID channelId; //작성채널
//    private UUID userId; //작성자
    private String content; //내용 입력
//    private List<UUID> attachmentIds; //첨부파일

    // N:1 관계
    private Channel channel;

    // N:1 관계
    private User author;

    // N:M 관계
    private List<BinaryContent> attachments;


    public Message(Channel channel, User author, String content, List<BinaryContent> attachmentx) {
        super();
        this.channel = channel;
        this.author = author;
        this.content = content;
        this.attachments = (attachments != null) ? new ArrayList<>(attachments) : new ArrayList<>();
    }

    public void setUpdate(String newContent){
        if(newContent != null && !newContent.equals(this.content)){
            this.content = newContent;
            setUpdatedAt(Instant.now());
        }
    }

    public void setAttachmentIds(List<BinaryContent> newAttachments) {
        this.attachments = (newAttachments != null) ? new ArrayList<>(newAttachments) : new ArrayList<>();
        setUpdatedAt(Instant.now());
    }
}

