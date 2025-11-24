package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateParams;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {


    private String name;
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChannelType type;


    private Channel(String name, String description, ChannelType type) {

        if (type == ChannelType.PUBLIC) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name is invalid");
            }
            if (description == null) {
                throw new IllegalArgumentException("description is null");
            }
        }

        this.name = name;
        this.description = description;
        this.type = type;
    }

    public static Channel createPublicChannel(String name, String description) {
        return new Channel(name, description, ChannelType.PUBLIC);
    }

    public static Channel createPrivateChannel() {
        return new Channel(null, null, ChannelType.PRIVATE);
    }

    public boolean updateName(String name) {
        if (name != null && !name.isBlank() && !name.equals(this.name)) {
            this.name = name;
            return true;
        }
        return false;
    }

    public boolean updateDescription(String description) {
        if (description != null && !description.equals(this.description)) { // NOTE: 설명란은 blank 가능하도록하기위해 isBlank는 체크는 제거
            // NOTE: trim 필요? 그게 설명일수도있는지(부가기능, 만약 내용은 같은데 여백같은거라면 trim하면 같아지는것도 고려)
            this.description = description;
            return true;
        }
        return false;
    }

    public void update(ChannelUpdateParams params) {
        this.updateName(params.name());
        this.updateDescription(params.description());
    }
}
