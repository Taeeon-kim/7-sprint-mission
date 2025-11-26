package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;


@Component
@ConditionalOnProperty(
        prefix = "discodeit.storage",
        name = "type",
        havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage{
     private final Path root;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
        this.root = Paths.get(rootPath);
    }

    @PostConstruct
    public void init() throws IOException {
        if(Files.notExists(root)) {
            Files.createDirectories(root);
        }
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    public UUID put(UUID binaryId, byte[] bytes) {
        try {
            Files.write(resolvePath(binaryId), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return binaryId;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + binaryId, e);
        }
    }

    @Override
    public InputStream get(UUID binaryId) {
        try {
            return Files.newInputStream(resolvePath(binaryId), StandardOpenOption.READ);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: " + binaryId, e);
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponseDto) {
        try {
            InputStream in = get(binaryContentResponseDto.id());
            ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

            MediaType mediaType = binaryContentResponseDto.contentType() != null
                    ? MediaType.parseMediaType(binaryContentResponseDto.contentType())
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + binaryContentResponseDto.fileName() + "\"")
                    .contentLength(binaryContentResponseDto.size() != null ? binaryContentResponseDto.size() : resource.contentLength())
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("다운로드 중 오류: " + binaryContentResponseDto.id(), e);
        }
    }
}
