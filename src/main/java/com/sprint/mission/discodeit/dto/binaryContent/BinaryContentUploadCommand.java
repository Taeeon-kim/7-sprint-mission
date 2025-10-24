package com.sprint.mission.discodeit.dto.binaryContent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@AllArgsConstructor
public class BinaryContentUploadCommand {
    private String fileName;
    private String contentType;
    private byte[] bytes;


    public static BinaryContentUploadCommand from(MultipartFile file) {
        try {
            return new BinaryContentUploadCommand(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }


    }
}
