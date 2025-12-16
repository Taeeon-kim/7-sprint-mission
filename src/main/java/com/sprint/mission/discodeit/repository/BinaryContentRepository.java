package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

    // 저장 : save

    // 조회 : findById

    List<BinaryContent> findAllById(UUID uuid);

    // 삭제 : delete

}
