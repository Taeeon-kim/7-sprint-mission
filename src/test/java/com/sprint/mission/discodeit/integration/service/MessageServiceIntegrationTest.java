package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.integration.fixtures.*;
import com.sprint.mission.discodeit.repository.*;

import com.sprint.mission.discodeit.service.MessageService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MessageServiceIntegrationTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BinaryContentRepository binaryContentRepository;
    @Autowired
    private ReadStatusRepository readStatusRepository;

    @BeforeEach
    void setUp() {

    }


    @Nested
    @DisplayName("sendToChannel")
    class MessageSendToChannel {

        @Test
        @DisplayName("[Integration][Positive] 메세지 전송(생성) - 저장 및 값 일치")
        void sendToChannel_then_persists() throws IOException {

            // Given

            User user = UserFixture.createUser(userRepository, userStatusRepository);
            Channel publicChannel = ChannelFixture.createPublicChannel(channelRepository);

            MockMultipartFile file = new MockMultipartFile(
                    "file",                // form field name
                    "test.png",            // filename
                    "image/png",           // content type
                    "test".getBytes()      // file content
            );

            BinaryContentUploadCommand binaryContentUploadCommand = BinaryContentUploadCommand.from(file);

            // When
            MessageResponseDto responseDto = messageService.sendMessageToChannel(
                    new MessageSendCommand(publicChannel.getId(), user.getId(), "message", List.of(binaryContentUploadCommand))
            );


            // Then
            Message message = messageRepository.findById(responseDto.id()).orElseThrow();
            List<BinaryContent> attachments = message.getAttachments();


            assertAll(
                    () -> assertEquals(responseDto.id(), message.getId()),
                    () -> assertEquals(user.getId(), message.getAuthor().getId()),
                    () -> assertEquals(publicChannel.getId(), message.getChannel().getId()),
                    () -> assertEquals("message", message.getContent()),
                    () -> assertEquals(binaryContentUploadCommand.contentType(), attachments.get(0).getContentType()),
                    () -> assertEquals(binaryContentUploadCommand.fileName(), attachments.get(0).getFileName()),
                    () -> assertEquals(binaryContentUploadCommand.bytes(), attachments.get(0).getBytes())
            );

        }


    }

    @Nested
    @DisplayName("getAllMessagesByChannelId")
    class getAllMessagesByChannelId {

        @Test
        @DisplayName("[Integration][Positive] 다중 메세지 조회 - 해당 채널의 메세지들 반환")
        void getAllMessagesByChannelId_returns_message_list() {

            // given
            User user = UserFixture.createUser(userRepository, userStatusRepository);
            Channel publicChannel = ChannelFixture.createPublicChannel(channelRepository);

            Message message1 = MessageFixture.sendMessage("message1", user, publicChannel, null, messageRepository);
            Message message2 = MessageFixture.sendMessage("message2", user, publicChannel, null, messageRepository);

            // when
            List<MessageResponseDto> allMessagesByChannelId = messageService.getAllMessagesByChannelId(publicChannel.getId());

            // then
            MessageResponseDto foundMessage1 = allMessagesByChannelId.stream()
                    .filter(message -> message.id() == message1.getId())
                    .findFirst()
                    .orElseThrow();

            MessageResponseDto foundMessage2 = allMessagesByChannelId.stream()
                    .filter(message -> message.id() == message2.getId())
                    .findFirst()
                    .orElseThrow();

            assertAll(
                    () -> assertEquals(2, allMessagesByChannelId.size()),
                    () -> assertEquals(message1.getId(), foundMessage1.id()),
                    () -> assertEquals(message2.getId(), foundMessage2.id()),
                    () -> assertEquals(message1.getContent(), foundMessage1.content()),
                    () -> assertEquals(message2.getContent(), foundMessage2.content()),
                    () -> assertEquals(message1.getChannel().getId(), foundMessage1.channelId()),
                    () -> assertEquals(message2.getChannel().getId(), foundMessage2.channelId()),
                    () -> assertEquals(message1.getAuthor().getId(), foundMessage1.authorId()),
                    () -> assertEquals(message2.getAuthor().getId(), foundMessage2.authorId())
            );


        }
    }

    @Nested
    @DisplayName("updateMessage")
    class updateMessage {

        @Autowired
        EntityManager em;

        @Test
        @DisplayName("[Integration][Positive] 메세지 수정 - 작성자가 수정하면 내용과 updatedAt이 반영된다")
        void updateMessage_updates_content_when_edits() throws InterruptedException {
            // given
            User user = UserFixture.createUser(userRepository, userStatusRepository);
            Channel publicChannel = ChannelFixture.createPublicChannel(channelRepository);

            Message message = MessageFixture.sendMessage(user, publicChannel, null, messageRepository);

            Instant before = message.getUpdatedAt(); // 또는 createdAt
            Thread.sleep(5);

            // when
            messageService.updateMessage(
                    MessageUpdateCommand.from(
                            MessageUpdateRequestDto.builder()
                                    .content("updated message")
                                    .build(),
                            message.getId()
                    ));

            em.flush();
            em.clear();

            // then
            Message findMessage = messageRepository.findById(message.getId()).orElseThrow();
            assertAll(
                    () -> assertEquals(message.getId(), findMessage.getId()),
                    () -> assertEquals("updated message", findMessage.getContent()),
                    () -> assertTrue(findMessage.getUpdatedAt().isAfter(before), "updatedAt이 갱신되어야 함")
            );


        }

    }

    @Nested()
    @DisplayName("deleteMessage")
    class deleteMessage {
        @Test
        @DisplayName("[Integration][Positive] 메세지 삭제 - 삭제 후 조회 시 예외")
        void deleteMessage_then_not_found() throws IOException {

            // given
            User user = UserFixture.createUser(userRepository, userStatusRepository);
            Channel publicChannel = ChannelFixture.createPublicChannel(channelRepository);

            BinaryContent binaryContent = BinaryContentFixture.createBinaryContent(binaryContentRepository);

            Message message = MessageFixture.sendMessage(user, publicChannel, List.of(binaryContent), messageRepository);

            List<Message> beforeMessages = messageRepository.findAllByChannelId(publicChannel.getId());
            assertTrue(beforeMessages.stream().anyMatch(m -> m.getId().equals(message.getId()))); //TODO: 객체를 포함하는걸로 봐야하는지 id들로만 빼서 id만 체크하는지 테스트 후 수정

            // when
            messageService.deleteMessage(message.getId());

            List<Message> afterMessages = messageRepository.findAllByChannelId(publicChannel.getId());

            //then
            assertAll(
                    // 1) 메시지 자체는 더 이상 조회되지 않아야 함
                    () -> assertTrue(messageRepository.findById(message.getId()).isEmpty()),

                    // 2) 첨부파일도 같이 삭제되었는지 (cascade / 도메인 정책 검증)
                    () -> assertTrue(binaryContentRepository.findById(binaryContent.getId()).isEmpty()),

                    // 3) 채널 메시지 목록에서 제거되었는지
                    () -> assertFalse(
                            afterMessages.stream().anyMatch(m -> m.getId().equals(message.getId())),
                            "채널 내 메시지 목록에서도 제거되어야 함"
                    )
            );

        }
    }

}
