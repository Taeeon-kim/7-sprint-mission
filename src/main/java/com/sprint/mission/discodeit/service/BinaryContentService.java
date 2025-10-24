package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    UUID uploadBinaryContent(BinaryContentUploadCommand command);

    BinaryContent getBinaryContent(UUID id);
    List<BinaryContent> getBinaryContentsByIds();

    void deleteBinaryContent(UUID id);
}
