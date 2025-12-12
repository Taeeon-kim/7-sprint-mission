package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BaseEntity {//implements Serializable {

//    private static final long serialVersionUID = 1L;
    private UUID uuid; //객체 식별 id

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    public BaseEntity(){
        this.uuid = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public BaseEntity(UUID uuid, Instant createdAt) {
        this.uuid = uuid;
        this.createdAt = createdAt;
    }
}
