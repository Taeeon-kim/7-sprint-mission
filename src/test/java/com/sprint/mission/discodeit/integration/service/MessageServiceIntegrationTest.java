package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.RoleType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.MessageReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MessageServiceIntegrationTest {

    private MessageRepository messageRepository;
    private ChannelRepository channelRepository;
    private UserRepository userRepository;
    private UserReader userReader;
    private ChannelReader channelReader;
    private MessageReader messageReader;

    private MessageService messageService;
    private BinaryContentRepository binaryContentRepository;


    @BeforeEach
    void setUp() {
        messageRepository = new JCFMessageRepository();
        channelRepository = new JCFChannelRepository();
        userRepository = new JCFUserRepository();
        userReader = new UserReader(userRepository);
        channelReader = new ChannelReader(channelRepository);
        messageReader = new MessageReader(messageRepository);
        binaryContentRepository = new JCFBinaryContentRepository();
        messageService = new BasicMessageService(messageRepository, channelRepository, userReader, channelReader, messageReader, binaryContentRepository);

    }


    @Nested
    @DisplayName("sendToChannel")
    class MessageSendToChannel {

        @Test
        @DisplayName("[Integration][Positive] 메세지 전송(생성) - 저장 및 값 일치")
        void sendToChannel_then_persists() throws IOException {

            // Given

            User sender = User.builder()
                    .email("aaa@exmple.com")
                    .profileId(null)
                    .role(RoleType.USER)
                    .password("password")
                    .nickname("nickname")
                    .build();

            userRepository.save(sender);
            Channel publicChannel = Channel.createPublicChannel("channel", "description");
            publicChannel.addUserId(sender.getId());
            channelRepository.save(publicChannel);


            MockMultipartFile file = new MockMultipartFile(
                    "file",                // form field name
                    "test.png",            // filename
                    "image/png",           // content type
                    "test".getBytes()      // file content
            );

            BinaryContent binaryContent = BinaryContent.builder()
                    .bytes(file.getBytes())
                    .contentType(file.getContentType())
                    .fileName(file.getOriginalFilename())
                    .build();

            BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);


            // When
            UUID messageId = messageService.sendMessageToChannel(
                    new MessageSendCommand(publicChannel.getId(), sender.getId(), "message", List.of(savedBinaryContent.getId()))
            );


            // Then
            Message message = messageRepository.findById(messageId).orElseThrow();
            List<UUID> attachmentIds = message.getAttachmentIds();
            UUID attachmentId = attachmentIds
                    .stream()
                    .filter(id -> id.equals(savedBinaryContent.getId()))
                    .findFirst()
                    .orElseThrow();

            BinaryContent binaryContentById = binaryContentRepository.findById(attachmentId).orElseThrow();

            assertAll(
                    () -> assertEquals(messageId, message.getId()),
                    () -> assertEquals(sender.getId(), message.getSenderId()),
                    () -> assertEquals(publicChannel.getId(), message.getChannelId()),
                    () -> assertEquals("message", message.getContent()),
                    () -> assertTrue(message.getAttachmentIds().contains(savedBinaryContent.getId())),
                    () -> assertEquals(savedBinaryContent.getContentType(), binaryContentById.getContentType()),
                    () -> assertEquals(savedBinaryContent.getFileName(), binaryContentById.getFileName()),
                    () -> assertEquals(savedBinaryContent.getBytes(), binaryContentById.getBytes()),
                    () -> assertEquals(savedBinaryContent.getCreatedAt(), binaryContentById.getCreatedAt())
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
            User user = User.builder()
                    .nickname("name")
                    .email("ee@exam.com")
                    .profileId(null)
                    .role(RoleType.USER)
                    .password("dsfsdfdf")
                    .build();
            userRepository.save(user);

            Channel publicChannel = Channel.createPublicChannel( "title", "description");
            publicChannel.addUserId(user.getId());
            channelRepository.save(publicChannel);

            Message message1 = messageRepository.save(new Message("message1", user.getId(), publicChannel.getId(), null));
            Message message2 = messageRepository.save(new Message("message2", user.getId(), publicChannel.getId(), null));

            publicChannel.addMessageId(message1.getId());
            publicChannel.addMessageId(message2.getId());
            channelRepository.save(publicChannel);

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
                    () -> assertEquals(message1.getChannelId(), foundMessage1.channelId()),
                    () -> assertEquals(message2.getChannelId(), foundMessage2.channelId()),
                    () -> assertEquals(message1.getSenderId(), foundMessage1.senderId()),
                    () -> assertEquals(message2.getSenderId(), foundMessage2.senderId())
            );


        }
    }

    @Nested
    @DisplayName("updateMessage")
    class updateMessage {

        @Test
        @DisplayName("[Integration][Positive] 메세지 수정 - 작성자가 수정하면 내용과 updatedAt이 반영된다")
        void updateMessage_updates_content_when_edits() throws InterruptedException {
            // given
            User user = User.builder()
                    .nickname("name")
                    .email("ee@exam.com")
                    .profileId(null)
                    .role(RoleType.USER)
                    .password("dsfsdfdf")
                    .build();
            userRepository.save(user);

            Channel publicChannel = Channel.createPublicChannel( "title", "description");
            publicChannel.addUserId(user.getId());
            channelRepository.save(publicChannel);

            Message message = messageRepository.save(new Message("message1", publicChannel.getId(), user.getId(), null));

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
            User user = User.builder()
                    .nickname("name")
                    .email("ee@exam.com")
                    .profileId(null)
                    .role(RoleType.USER)
                    .password("dsfsdfdf")
                    .build();
            userRepository.save(user);

            Channel publicChannel = Channel.createPublicChannel("title", "description");
            publicChannel.addUserId(user.getId());
            channelRepository.save(publicChannel);


            MockMultipartFile file = new MockMultipartFile(
                    "file",                // form field name
                    "test.png",            // filename
                    "image/png",           // content type
                    "test".getBytes()      // file content
            );

            BinaryContent binaryContent = BinaryContent.builder()
                    .bytes(file.getBytes())
                    .contentType(file.getContentType())
                    .fileName(file.getOriginalFilename())
                    .build();

            BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
            Message savedMessage = messageRepository.save(
                    new Message("message1",
                            user.getId(),
                            publicChannel.getId(),
                            List.of(savedBinaryContent.getId())
                    )
            );
            publicChannel.addMessageId(savedMessage.getId());
            channelRepository.save(publicChannel);

            Channel reloadedChannel = channelRepository.findById(publicChannel.getId()).orElseThrow();

            assertTrue(reloadedChannel.getMessageIds().contains(savedMessage.getId()));
            // when
            messageService.deleteMessage(savedMessage.getId());

            //then
            assertAll(
                    () -> assertThrows(NoSuchElementException.class, () -> messageRepository.findById(savedMessage.getId()).orElseThrow()),
                    () -> assertThrows(NoSuchElementException.class, () -> binaryContentRepository.findById(savedBinaryContent.getId()).orElseThrow()),
                    () -> assertFalse(reloadedChannel.getMessageIds().contains(savedMessage.getId()), "채널 내 메시지 목록에서도 제거되어야 함")

            );

        }
    }

}
