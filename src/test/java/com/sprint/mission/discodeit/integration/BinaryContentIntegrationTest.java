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
    private InMemoryStore store;
    private BinaryContentRepository binaryContentRepository;
    private BinaryContentService binaryContentService;

    @BeforeEach
    void setUp() {
        store = new InMemoryStore();
        binaryContentRepository = new JCFBinaryContentRepository(store.binaryContents);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class Upload{

        @Test
        @DisplayName("[Integration][Flow] 파일 업로드 - 업로드 후 id 반환 및 저장 확인")
        void upload_then_returns_id_and_persists() {
            //given
            BinaryContentUploadCommand command = new BinaryContentUploadCommand("test.png", "image/png", "test".getBytes());

            //when
            UUID uuid = binaryContentService.uploadBinaryContent(command);

            //then
            assertNotNull(uuid);

            BinaryContent binaryContent = binaryContentRepository.findById(uuid).get();

            assertEquals(command.getFileName(), binaryContent.getFileName());
            assertEquals(command.getContentType(), binaryContent.getContentType());
            assertArrayEquals(command.getBytes(), binaryContent.getBytes());

        }

    }
}
