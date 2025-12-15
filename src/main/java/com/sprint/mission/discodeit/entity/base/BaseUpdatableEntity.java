package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class BaseUpdatableEntity extends BaseEntity {

    @LastModifiedDate
    private Instant updatedAt;

    public BaseUpdatableEntity(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
