package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UUID uploadBinaryContent(BinaryContentUploadCommand command) {

        BinaryContent binaryContent = new BinaryContent(
                command.getFileName(),
                command.getContentType(),
                command.getBytes());

        BinaryContent saved = binaryContentRepository.save(binaryContent);
        return saved.getId();
    }

    @Override
    public BinaryContent getBinaryContent(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 파일을 찾을수 없습니다."));
    }

    @Override
    public List<BinaryContent> getBinaryContentsByIds(List<UUID> ids) {
        return binaryContentRepository.findAllByIds(ids);
    }

    @Override
    public void deleteBinaryContent(UUID id) {

    }
}
