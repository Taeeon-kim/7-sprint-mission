package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BaseEntity {//implements Serializable {

//    private static final long serialVersionUID = 1L;
    private UUID uuid; //객체 식별 id
    private Instant createdAt; //객체 생성 시간 //유닉스 타임스탬프. 눈으로보는 시간X

    public BaseEntity(){
        this.uuid = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public BaseEntity(UUID uuid, Instant createdAt) {
        this.uuid = uuid;
        this.createdAt = createdAt;
    }
}
