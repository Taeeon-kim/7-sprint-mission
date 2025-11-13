package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        BinaryContent entity = new BinaryContent(
//                UUID.randomUUID(),
//                Instant.now(),
                binaryContentCreateRequestDto.getFileName(),
                binaryContentCreateRequestDto.getContentType(),
                binaryContentCreateRequestDto.getBytes()
        );
        binaryContentRepository.save(entity);
        return BinaryContentResponseDto.from(entity);
    }

    @Override
    public BinaryContentResponseDto find(UUID uuid) {
        BinaryContent entity = binaryContentRepository.findById(uuid);
        if (entity == null) {
            throw new RuntimeException("Binary content를 찾을 수 없음");
        }
        return BinaryContentResponseDto.from(entity);
    }

    @Override
    public List<BinaryContentResponseDto> findAllById(List<UUID> uuids) {
        return uuids.stream()
                .map(this::find)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID uuid) {
        BinaryContent binaryContent = binaryContentRepository.findById(uuid);
        if (binaryContent == null) {
            throw new RuntimeException("BinaryContent를 찾을 수 없다.");
        }
        binaryContentRepository.delete(uuid);
    }
}
