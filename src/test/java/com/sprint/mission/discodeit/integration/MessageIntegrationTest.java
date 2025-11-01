package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.message.MessageSendRequestDto;
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
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.MessageReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import com.sprint.mission.discodeit.store.InMemoryStore;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MessageIntegrationTest {

    private InMemoryStore store;
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
        store = new InMemoryStore();
        messageRepository = new JCFMessageRepository(store.messages);
        channelRepository = new JCFChannelRepository(store.channels);
        userRepository = new JCFUserRepository(store.users);
        userReader = new UserReader(userRepository);
        channelReader = new ChannelReader(channelRepository);
        messageReader = new MessageReader(messageRepository);
        binaryContentRepository = new JCFBinaryContentRepository(store.binaryContents);
        messageService = new BasicMessageService(messageRepository, channelRepository, userReader, channelReader, messageReader);

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
                    .phoneNumber("010-2222-3333")
                    .build();

            userRepository.save(sender);
            Channel publicChannel = Channel.createPublicChannel(sender.getId(), "channel", "description");
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
                    MessageSendRequestDto.builder()
                            .channelId(publicChannel.getId())
                            .senderId(sender.getId())
                            .content("message")
                            .binaryFileIds(List.of(savedBinaryContent.getId()))
                            .build()
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

}
