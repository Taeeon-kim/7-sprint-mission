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
public class Channel extends BaseUpdatableEntity {

    private String channelName;
    private ChannelType channelType;
    private String discription;
//    private List<UUID> participantIds = new ArrayList<>();

    // 1:N 관계
    private List<Message> messages;

    // N:M 관계
    private List<User> member;

    // 1:N 관계
    private List<ReadStatus> readStatuses;

    public void setUpdate(String newChannelName, String newDiscription) {
        if(this.channelType == ChannelType.PRIVATE){
            throw new UnsupportedOperationException("Private channels cannot be updated");
        }
        if(newChannelName != null && !newChannelName.equals(this.channelName)) {
            this.channelName = newChannelName;
        }
        if(newDiscription != null && !newDiscription.equals(this.discription)) {
            this.discription = newDiscription;
        }
        if(newChannelName != null || newDiscription != null) {
            setUpdatedAt(Instant.now());
        }
    }

    //PUBLIC
    public Channel(String channelName, ChannelType channelType, String discription) {
        super();
        this.channelName = channelName;
        this.channelType = channelType;
        this.discription = discription;
    }

    public Channel(ChannelType channelType) {
        super();
        this.channelType = channelType;
        this.channelName = null;
        this.discription = null;
    }
}
