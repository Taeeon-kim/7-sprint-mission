package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {

    UUID put(UUID binaryId, byte[] bytes);

    InputStream get(UUID binaryId);

    ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponseDto);
}
