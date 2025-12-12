package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BaseUpdatableEntity extends BaseEntity{

    private Instant updatedAt; //객체 수정 시간

    public BaseUpdatableEntity() {
        this.updatedAt = Instant.now();
    }

    public BaseUpdatableEntity(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
