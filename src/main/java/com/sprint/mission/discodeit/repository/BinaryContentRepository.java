package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {

    // 저장
    BinaryContent save(BinaryContent binaryContent);

    // 조회
    BinaryContent findById(UUID uuid);

    List<BinaryContent> findAllById(UUID uuid);

    // 삭제
    void delete(UUID uuid);

    List<BinaryContent> findAll();
}
