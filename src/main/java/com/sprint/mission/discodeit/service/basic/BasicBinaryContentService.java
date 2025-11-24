package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    @Transactional
    public UUID uploadBinaryContent(BinaryContentUploadCommand command) {

        BinaryContent binaryContent = new BinaryContent(
                command.fileName(),
                command.contentType(),
                command.size(),
                command.bytes());

        BinaryContent saved = binaryContentRepository.save(binaryContent);
        return saved.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentResponseDto getBinaryContent(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 파일을 찾을수 없습니다."));
        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentResponseDto> getBinaryContentsByIds(List<UUID> ids) {
        List<BinaryContent> allByIds = binaryContentRepository.findAllById(ids);
        return allByIds.stream()
                .map(BinaryContentResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void deleteBinaryContent(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("전달값이 잘못되었습니다.");
        }
        binaryContentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentResponseDto> getAllBinaryContents() {
        List<BinaryContent> all = binaryContentRepository.findAll();
        return all.stream()
                .map(BinaryContentResponseDto::from)
                .toList();
    }
}
