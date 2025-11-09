package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {

    // 사용자 상태
    void save(UserStatus userStatus);

    // 유저 한 명 최신 접속 상태 확인
    UserStatus findByUserId(UUID userId);

    // 전체 사용자 상태 목록
    List<UserStatus> findAll();

    // 계정 삭제시 제거
    void delete(UUID uuid);

}
