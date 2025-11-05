package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    UUID uploadBinaryContent(BinaryContentUploadCommand command);

    BinaryContentResponseDto getBinaryContent(UUID id);

    List<BinaryContentResponseDto> getBinaryContentsByIds(List<UUID> ids);

    void deleteBinaryContent(UUID id);
}
