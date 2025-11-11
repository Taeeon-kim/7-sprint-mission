package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryContentServiceIntegrationTest {
    private BinaryContentRepository binaryContentRepository;
    private BinaryContentService binaryContentService;

    @BeforeEach
    void setUp() {

        binaryContentRepository = new JCFBinaryContentRepository();
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("uploadBinaryContent")
    class Upload {

        @Test
        @DisplayName("[Integration][Flow][Positive] 파일 업로드 - 필수 필드 충족, 업로드 후 id 반환 및 저장 확인")
        void uploadBinaryContent_then_returns_id_and_persists() {
            //given
            // TODO: 지금은 예시로 직접 텍스트값으로 fileName, contentType, bytes[] 값을 넣어줬는데 시간이 난다면 실제 이미지파일 넣는걸로 테스트 해볼것
            MockMultipartFile file = new MockMultipartFile(
                    "file",                // form field name
                    "test.png",            // filename
                    "image/png",           // content type
                    "test".getBytes()      // file content
            );
            BinaryContentUploadCommand command = BinaryContentUploadCommand.from(file);

            //when
            UUID uuid = binaryContentService.uploadBinaryContent(command);

            //then
            assertNotNull(uuid);

            BinaryContent binaryContent = binaryContentRepository.findById(uuid).orElseThrow();

            assertEquals(command.fileName(), binaryContent.getFileName());
            assertEquals(command.contentType(), binaryContent.getContentType());
            assertArrayEquals(command.bytes(), binaryContent.getBytes());

        }
    }

    @Nested
    @DisplayName("getBinaryContent")
    class getBinaryContent {
        @Test
        @DisplayName("[Integration][Flow][Positive] 파일 조회 - 요청 id에 해당하는 binaryContent 반환")
        void getBinaryContent_returns_saved_content() {
            //given
            BinaryContent updateContent = new BinaryContent("test.png", "image/png", "test".getBytes());
            BinaryContent saved = binaryContentRepository.save(updateContent);

            // when
            BinaryContentResponseDto responseDtos = binaryContentService.getBinaryContent(saved.getId());

            // then
            assertEquals(saved.getId(), responseDtos.id());
            assertEquals(saved.getFileName(), responseDtos.fileName());
            assertEquals(saved.getContentType(), responseDtos.contentType());

            assertArrayEquals(saved.getBytes(), responseDtos.bytes()); // 내용비교를 위해 순회하면 하나씩 비교, 원소 수, 순서, 값 모두 같으면 통과
        }

        @Test
        @DisplayName("[Integration][Flow][Negative] 파일 조회 - 존재하지 않는 Id로 조회시 NoSuchElementException 예외")
        void getBinaryContent_thorws_when_not_found() {
            // given
            UUID id = UUID.randomUUID();

            // when & then
            assertThrows(NoSuchElementException.class, () -> binaryContentService.getBinaryContent(id));
        }
    }

    @Nested
    @DisplayName("getBinaryContentsByIds")
    class getBinaryContentsByIds {

        @Test
        @DisplayName("[Integration][Flow][Positive] 파일 다중 조회 - 요청 ids에 해당하는 binaryContent List 반환")
        void getBinaryContentsByIds_returns_saved_contents() {
            //given
            BinaryContent updateContent = new BinaryContent("test.png", "image/png", "test".getBytes());
            BinaryContent updateContent2 = new BinaryContent("test2.png", "image/png", "test222".getBytes());
            BinaryContent saved = binaryContentRepository.save(updateContent);
            BinaryContent saved2 = binaryContentRepository.save(updateContent2);

            // when
            List<BinaryContentResponseDto> responseDtos = binaryContentService.getBinaryContentsByIds(List.of(saved.getId(), saved2.getId()));

            // then

            assertAll(
                    () -> assertEquals(2, responseDtos.size()),
                    () -> assertEquals(saved.getId(), responseDtos.get(0).id()),
                    () -> assertEquals(saved2.getId(), responseDtos.get(1).id()),
                    () -> assertEquals(saved.getFileName(), responseDtos.get(0).fileName()),
                    () -> assertEquals(saved2.getFileName(), responseDtos.get(1).fileName()),
                    () -> assertEquals(saved.getContentType(), responseDtos.get(0).contentType()),
                    () -> assertEquals(saved2.getContentType(), responseDtos.get(1).contentType())
            );

        }


        @Test
        @DisplayName("[Integration][Flow][Negative] 파일 다중조회 - 없는 ids로 조회시 빈배열 반환")
        void getBinaryContentsByIds_returns_empty_list_when_not_found() {
            //given
            List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());

            // when
            List<BinaryContentResponseDto> responseDtos = binaryContentService.getBinaryContentsByIds(ids);

            // then
            assertTrue(responseDtos.isEmpty());
        }
    }

    @Nested
    @DisplayName("deleteBinaryContent")
    class deleteBinaryContent {

        @Test
        @DisplayName("[Integration][Flow][Positive] 파일 삭제 - 삭제 후 조회 불가 & 개수 감소")
        void deleteBinaryContent_then_not_found_and_size_decreased() {
            // given
            BinaryContent updateContent = new BinaryContent("test.png", "image/png", "test".getBytes());
            BinaryContent saved = binaryContentRepository.save(updateContent);
            long before = binaryContentRepository.findAll().size();

            // when
            binaryContentService.deleteBinaryContent(saved.getId());

            // then
            long after = binaryContentRepository.findAll().size();
            assertEquals(before - 1, after);
            assertThrows(NoSuchElementException.class, ()-> binaryContentService.getBinaryContent(saved.getId()));
        }

    }
}
