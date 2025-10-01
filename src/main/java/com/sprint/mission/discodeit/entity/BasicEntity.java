package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public abstract class BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

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

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("updatedAt is null");
        }

        if (updatedAt < this.updatedAt) {
            throw new IllegalStateException("업데이트 날짜가 잘못되었습니다.");
        }

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
