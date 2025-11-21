package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BaseUpdatableEntity extends BasicEntity{
    private Instant updatedAt;

    public BaseUpdatableEntity() {
        this.updatedAt = Instant.now();
    }

    public void setUpdatedAt(Instant updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("updatedAt is null");
        }

        if (updatedAt.isBefore(this.updatedAt)) {
            throw new IllegalStateException("업데이트 날짜가 잘못되었습니다.");
        }

        this.updatedAt = updatedAt;
    }
}
