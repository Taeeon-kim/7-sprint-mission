package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class BaseUpdatableEntity extends BasicEntity {

    @LastModifiedDate
    private Instant updatedAt;

    // TODO: 테스트코드 등 사용하는 코드 모두 수정완료되면 아래 코드 지울것
//    public void setUpdatedAt(Instant updatedAt) {
//        if (updatedAt == null) {
//            throw new IllegalArgumentException("updatedAt is null");
//        }
//
//        if (updatedAt.isBefore(this.updatedAt)) {
//            throw new IllegalStateException("업데이트 날짜가 잘못되었습니다.");
//        }
//
//        this.updatedAt = updatedAt;
//    }
}
