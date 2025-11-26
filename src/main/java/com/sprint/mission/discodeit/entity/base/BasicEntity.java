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
@MappedSuperclass // JPA Entity 상속 받게 부모 클래스로 인정하도록, 이 클래스는 테이블 자체인 엔티티가 아닌, 컬럼 정보만 자식에게 제공하기 위해서 만든 클래스
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class BasicEntity { // abstract 붙여 직접 생성하지 않고 반드시 상속을 통해 구현되어야 한다는 것을 강조하기위해 사용.

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
