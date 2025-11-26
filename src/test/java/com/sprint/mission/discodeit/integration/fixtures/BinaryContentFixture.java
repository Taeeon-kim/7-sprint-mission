package com.sprint.mission.discodeit.integration.fixtures;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

public class BinaryContentFixture {
    private BinaryContentFixture() {
    }

    /**
     * 기본 MockMultipartFile 하나 만들어줌 (이미지 파일 흉내)
     */
    public static MockMultipartFile defaultMockFile() {
        return new MockMultipartFile(
                "file",           // form field name
                "test.png",       // filename
                "image/png",      // content type
                "test".getBytes() // content
        );
    }

    /**
     * 기본 MockMultipartFile을 기반으로 BinaryContent를 저장하고 리턴
     */
    public static BinaryContent createBinaryContent(BinaryContentRepository binaryContentRepository) throws IOException {
        MockMultipartFile file = defaultMockFile();
        return binaryContentRepository.save(BinaryContent.builder()
                .contentType(file.getContentType())
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .build());
    }

    /**
     * 원하는 MockMultipartFile로 BinaryContent를 저장하고 싶을 때
     */
    public static BinaryContent createBinaryContent(BinaryContentRepository binaryContentRepository, MockMultipartFile file) throws IOException {
        return binaryContentRepository.save(BinaryContent.builder()
                .contentType(file.getContentType())
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .build());
    }
}
