package com.sprint.mission.discodeit.dto.binaryContent;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentUploadCommand(
        String fileName,
        String contentType,
        byte[] bytes,
        Long size
) {
    public static BinaryContentUploadCommand from(MultipartFile file) {
        try {
            return new BinaryContentUploadCommand(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),
                    file.getSize()
                    );
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
    }
}
