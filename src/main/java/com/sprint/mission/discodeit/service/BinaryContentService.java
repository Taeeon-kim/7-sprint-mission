package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto);

    BinaryContentResponseDto find(UUID uuid);

    List<BinaryContentResponseDto> findAllById(List<UUID> uuids);

    void delete(UUID uuid);
}
