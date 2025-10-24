package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BasicBinaryContentService implements BinaryContentService {
    @Override
    public UUID uploadBinaryContent(BinaryContentUploadCommand command) {
        return null;
    }

    @Override
    public BinaryContent getBinaryContent(UUID id) {
        return null;
    }

    @Override
    public List<BinaryContent> getBinaryContentsByIds() {
        return List.of();
    }

    @Override
    public void deleteBinaryContent(UUID id) {

    }
}
