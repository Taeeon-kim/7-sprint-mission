package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChannelType type;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String discription;
//    private List<UUID> participantIds = new ArrayList<>();

    // 1:N 관계
    private List<Message> messages;

    // N:M 관계
    private List<User> member;

    // 1:N 관계
    private List<ReadStatus> readStatuses;

    public void setUpdate(String newName, String newDiscription) {
        if(this.type == ChannelType.PRIVATE){
            throw new UnsupportedOperationException("Private channels cannot be updated");
        }
        if(newName != null && !newName.equals(this.name)) {
            this.name = newName;
        }
        if(newDiscription != null && !newDiscription.equals(this.discription)) {
            this.discription = newDiscription;
        }
        if(newName != null || newDiscription != null) {
            setUpdatedAt(Instant.now());
        }
    }

    //PUBLIC
    public Channel(String name, ChannelType type, String discription) {
        super();
        this.name = name;
        this.type = type;
        this.discription = discription;
    }

    public Channel(ChannelType type) {
        super();
        this.type = type;
        this.name = null;
        this.discription = null;
    }
}
