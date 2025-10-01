package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {

    private final UUID uuid; //객체 식별 id
    private final long createAt; //객체 생성 시간 //유닉스 타임스탬프. 눈으로보는 시간X
    private long updatedAt; //객체 수정 시간

    public BaseEntity() {
        this.uuid = UUID.randomUUID(); //자동 생성
        this.createAt = System.currentTimeMillis(); //현재 시각 받아옴
        this.updatedAt = this.createAt; //초기엔 동일.
    }

    public UUID getId() {
        return uuid;
    }

    public long getCreateAt() {
        return createAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + uuid +
                ", createAt=" + createAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
