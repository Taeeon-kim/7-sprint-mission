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

    protected BasicEntity(BasicEntity user) {
        this.id = user.id; // 클래스가 같으니 getId아닌 직접참조
        this.createdAt = user.createdAt;
        this.updatedAt = user.updatedAt;
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

    @Override
    public String toString() {
        return "BasicEntity{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
