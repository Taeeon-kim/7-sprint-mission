package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BinaryContentServiceIntegrationTest {

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Autowired
    private BinaryContentService binaryContentService;

    @BeforeEach
    void setUp() {
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

        }
    }

    @Nested
    @DisplayName("getBinaryContent")
    class getBinaryContent {
        @Test
        @DisplayName("[Integration][Flow][Positive] 파일 조회 - 요청 id에 해당하는 binaryContent 반환")
        void getBinaryContent_returns_saved_content() {
            //given
            BinaryContent updateContent = new BinaryContent("test.png", "image/png", (long) "test".length());
            BinaryContent saved = binaryContentRepository.save(updateContent);

            // when
            BinaryContentResponseDto responseDtos = binaryContentService.getBinaryContent(saved.getId());

            // then
            assertEquals(saved.getId(), responseDtos.id());
            assertEquals(saved.getFileName(), responseDtos.fileName());
            assertEquals(saved.getContentType(), responseDtos.contentType());

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
            BinaryContent updateContent = new BinaryContent("test.png", "image/png", (long) "test".length());
            BinaryContent updateContent2 = new BinaryContent("test2.png", "image/png", (long) "test".length());
            BinaryContent saved = binaryContentRepository.save(updateContent);
            BinaryContent saved2 = binaryContentRepository.save(updateContent2);

            // when
            List<BinaryContentResponseDto> responseDtos =
                    binaryContentService.getBinaryContentsByIds(List.of(saved.getId(), saved2.getId()));

// then
            Map<UUID, BinaryContentResponseDto> mapResponse = responseDtos.stream()
                    .collect(Collectors.toMap(BinaryContentResponseDto::id, Function.identity()));

            assertAll(
                    () -> assertEquals(2, responseDtos.size()),
                    () -> assertEquals(saved.getFileName(), mapResponse.get(saved.getId()).fileName()),
                    () -> assertEquals(saved2.getFileName(), mapResponse.get(saved2.getId()).fileName()),
                    () -> assertEquals(saved.getContentType(), mapResponse.get(saved.getId()).contentType()),
                    () -> assertEquals(saved2.getContentType(), mapResponse.get(saved2.getId()).contentType())
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
            BinaryContent updateContent = new BinaryContent("test.png", "image/png", (long) "test".length());
            BinaryContent saved = binaryContentRepository.save(updateContent);
            long before = binaryContentRepository.findAll().size();

            // when
            binaryContentService.
                    deleteBinaryContent(saved.getId());

            // then
            long after = binaryContentRepository.findAll().size();
            assertEquals(before - 1, after);
            assertThrows(NoSuchElementException.class, () -> binaryContentService.getBinaryContent(saved.getId()));
        }

    }
}
