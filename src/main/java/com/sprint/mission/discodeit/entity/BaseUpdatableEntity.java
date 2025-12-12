package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
public class BaseUpdatableEntity extends BaseEntity{

    @LastModifiedDate
    private Instant updatedAt;

    public BaseUpdatableEntity() {
        this.updatedAt = Instant.now();
    }

    public BaseUpdatableEntity(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
