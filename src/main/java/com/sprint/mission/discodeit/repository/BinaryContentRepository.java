package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {

    // 저장
    void save(BinaryContent binaryContent);

    // 조회
    BinaryContent findById(UUID uuid);

    // 특정 유저 이미지
    List<BinaryContent> findByUserId(UUID userId);

    // 특정 메시지 이미지 조회
    List<BinaryContent> findByChannelId(UUID channelId);

    // 삭제
    void delete(UUID uuid);

}
