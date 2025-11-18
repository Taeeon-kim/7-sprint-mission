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
public class Channel extends BaseEntity {
    //채널 id, 생성, 수정은 BaseEntity에

    private String channelName;
    private ChannelType channelType;
    private List<UUID> participantIds = new ArrayList<>();

    public void setUpdate(String newChannelName) {
        if(newChannelName != null && !newChannelName.equals(this.channelName)){
            this.channelName = newChannelName;
            setUpdatedAt(Instant.now());
        }
    }

    //PUBLIC
    public Channel(String channelName, ChannelType channelType) {
        super();
        this.channelName = channelName;
        this.channelType = channelType;
    }

    public Channel(List<UUID> participantIds, ChannelType channelType) {
        super();
        this.participantIds = participantIds;
        this.channelType = channelType;
        this.channelName = null;
    }
}
