package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BasicEntity implements Serializable {
    private final UUID id; // TODO: JPA로 바꾸고 Final 풀어주고, 리플렉션 되도록
    private final Instant createdAt;

    protected BasicEntity() { // NOTE: 직접 생성 불가 + 자식에서 호출하도록
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    protected BasicEntity(BasicEntity user) {
        this.id = user.id; // 클래스가 같으니 getId아닌 직접참조
        this.createdAt = user.createdAt;
    }

    @Override
    public String toString() {
        return "BasicEntity{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}
