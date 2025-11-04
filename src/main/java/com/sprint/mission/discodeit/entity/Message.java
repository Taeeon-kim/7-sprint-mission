package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter @ToString
@AllArgsConstructor
public class Message extends BaseEntity {

    private UUID channelId; //작성채널
    private UUID userId; //작성자
    private String content; //내용 입력
    private List<UUID> attachmentIds; //첨부파일

    public void setUpdate(String newContent){
        if(newContent != null && !newContent.equals(this.content)){
            this.content = newContent;
            setUpdatedAt(Instant.now());
        }
    }

}

