package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {

    // 유저-채널 읽음 상태 저장
    void save(ReadStatus readStatus);

    // 특정 유저가 특정 채널에서 마지막으로 읽은 시점을 가져옴
    ReadStatus findByUserAndChannel(UUID userId, UUID channelId);

    // 유저가 여러 채널을 어디까지 읽었는지 목록 조회
    List<ReadStatus> findByUserId(UUID userId);

    // 사용자가 채널을 스크롤하거나 채널을 열었을 때 호출.
    void updateLastAt(UUID userId, UUID channelId);

    // 사용자가 채널을 떠난 경우 삭제
    void delete(UUID uuid);

}
