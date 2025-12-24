package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

    // 사용자 상태 : save

    Optional<UserStatus> findByUser_Id(UUID userId);

    // 전체 사용자 상태 목록 : findAll

    // 계정 삭제시 제거 : delete

}
