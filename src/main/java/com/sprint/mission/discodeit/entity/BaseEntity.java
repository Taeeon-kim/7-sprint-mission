package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @ToString
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID uuid; //객체 식별 id
    private final Instant createAt; //객체 생성 시간 //유닉스 타임스탬프. 눈으로보는 시간X
    private Instant updatedAt; //객체 수정 시간

    public BaseEntity() {
        this.uuid = UUID.randomUUID(); //자동 생성
        createAt = Instant.now();
        this.updatedAt = this.createAt; //초기엔 동일.
    }
}
