package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

    // 사용자 상태 : save

    // 유저 한 명 최신 접속 상태 확인
    UserStatus findByUserId(UUID userId);

    // 전체 사용자 상태 목록 : findAll

    // 계정 삭제시 제거 : delete

}
