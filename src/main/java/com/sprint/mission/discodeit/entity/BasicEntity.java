package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class BasicEntity {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    protected BasicEntity() { // NOTE: 직접 생성 불가 + 자식에서 호출하도록
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();

    }

    public BasicEntity(UUID id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() { // NOTE: 캡슐화, 정보은닉
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
