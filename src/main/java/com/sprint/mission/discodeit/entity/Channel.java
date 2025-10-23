package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter @ToString
public class Channel extends BaseEntity {
    //채널 id, 생성, 수정은 BaseEntity에

    private ChannelType type;
    private String chanName; //채널 이름String

    public Channel(ChannelType type, String chanName) {
        super();
        this.type = type;
        this.chanName = chanName;
    }

    public void setChanName(String chanName) {
        this.chanName = chanName;
        setUpdatedAt(Instant.now()); //채널 이름 변경 시
    }
}
