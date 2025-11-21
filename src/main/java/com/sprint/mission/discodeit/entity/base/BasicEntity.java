package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass // JPA Entity 상속 받게 부모 클래스로 인정하도록
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class BasicEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Override
    public String toString() {
        return "BasicEntity{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}
