package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public UUID uploadBinaryContent(BinaryContentUploadCommand command) {

        BinaryContent binaryContent = new BinaryContent(
                command.fileName(),
                command.contentType(),
                command.size());

        BinaryContent saved = null;
        saved = binaryContentRepository.save(binaryContent);
        try {
            binaryContentStorage.put(saved.getId(), command.bytes());
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 파일 실패", e);
        }
        // 키id로 값 bytes 저장
        return saved.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentResponseDto getBinaryContent(UUID id) {
        log.debug("binary content 조회 시도 id={}", id);
        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(() -> new BinaryContentNotFoundException(id));
        log.debug("binary content 조회 성공 binaryId={}", binaryContent.getId()); // NOTE: read는 너무많은 info 발생하므로 debug로
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentResponseDto> getBinaryContentsByIds(List<UUID> ids) {
        List<BinaryContent> allByIds = binaryContentRepository.findAllById(ids);
        return allByIds.stream()
                .map(binaryContentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteBinaryContent(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }
        binaryContentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentResponseDto> getAllBinaryContents() {
        log.debug("전체 binary content 조회 시도");
        List<BinaryContent> all = binaryContentRepository.findAll();
        log.debug("전체 binary content 조회 성공 count={}", all.size());
        return all.stream()
                .map(binaryContentMapper::toDto)
                .toList();
    }
}
