package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id; // TODO: JPA로 바꾸고 Final 풀어주고, 리플렉션 되도록
    private final Instant createdAt;
    private Instant updatedAt;

    protected BasicEntity() { // NOTE: 직접 생성 불가 + 자식에서 호출하도록
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

    }

    protected BasicEntity(BasicEntity user) {
        this.id = user.id; // 클래스가 같으니 getId아닌 직접참조
        this.createdAt = user.createdAt;
        this.updatedAt = user.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("updatedAt is null");
        }

        if (updatedAt.isBefore(this.updatedAt)) {
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
