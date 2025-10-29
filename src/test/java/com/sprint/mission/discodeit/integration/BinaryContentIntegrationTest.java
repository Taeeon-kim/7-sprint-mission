package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.store.InMemoryStore;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryContentIntegrationTest {
    private final InMemoryStore store = new InMemoryStore();
    private BinaryContentRepository binaryContentRepository;
    private BinaryContentService binaryContentService;

    @BeforeEach
    void setUp() {

        binaryContentRepository = new JCFBinaryContentRepository(store.binaryContents);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
    }

    @AfterEach
    void tearDown() {
        store.binaryContents.clear();
    }

    @Nested
    @DisplayName("uploadBinaryContent")
    class Upload {

        @Test
        @DisplayName("[Integration][Flow] 파일 업로드 - 필수 필드 충족, 업로드 후 id 반환 및 저장 확인")
        void uploadBinaryContent_then_returns_id_and_persists() {
            //given
            // TODO: 지금은 예시로 직접 텍스트값으로 fileName, contentType, bytes[] 값을 넣어줬는데 시간이 난다면 실제 이미지파일 넣는걸로 테스트 해볼것
            BinaryContentUploadCommand command = new BinaryContentUploadCommand("test.png", "image/png", "test".getBytes());

            //when
            UUID uuid = binaryContentService.uploadBinaryContent(command);

            //then
            assertNotNull(uuid);

            BinaryContent binaryContent = binaryContentRepository.findById(uuid).orElseThrow();

            assertEquals(command.getFileName(), binaryContent.getFileName());
            assertEquals(command.getContentType(), binaryContent.getContentType());
            assertArrayEquals(command.getBytes(), binaryContent.getBytes());

        }
    }
}
