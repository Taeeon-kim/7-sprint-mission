package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@MappedSuperclass // JPA Entity 상속 받게 부모 클래스로 인정하도록, 이 클래스는 테이블 자체인 엔티티가 아닌, 컬럼 정보만 자식에게 제공하기 위해서 만든 클래스
public abstract class BaseUpdatableEntity extends BasicEntity {

    @LastModifiedDate
    private Instant updatedAt; // TODO: 실제 값이 안들어가는 중 추후 이부분 고칠것

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
