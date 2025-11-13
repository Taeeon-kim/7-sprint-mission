package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentApi {

    ResponseEntity<BinaryContentResponseDto> getBinaryContent(UUID id);

    ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContentsByIds(List<UUID> ids);

    ResponseEntity<UUID> createBinaryContent(MultipartFile file);

}
