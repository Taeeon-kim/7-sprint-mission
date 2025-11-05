package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID uuid; //객체 식별 id
    private Instant createAt; //객체 생성 시간 //유닉스 타임스탬프. 눈으로보는 시간X
    private Instant updatedAt; //객체 수정 시간

    public BaseEntity(){
        this.uuid = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public BaseEntity(UUID uuid, Instant createAt, Instant updatedAt) {
        this.uuid = uuid;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
    }
}
