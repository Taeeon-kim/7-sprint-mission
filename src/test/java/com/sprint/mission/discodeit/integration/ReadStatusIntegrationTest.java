package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.RoleType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import com.sprint.mission.discodeit.store.InMemoryStore;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReadStatusIntegrationTest {
    private final InMemoryStore store = new InMemoryStore();
    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private ReadStatusRepository readStatusRepository;
    private ReadStatusService readStatusService;
    private UserReader userReader;
    private ChannelReader channelReader;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository(store.users);
        channelRepository = new JCFChannelRepository(store.channels);
        readStatusRepository = new JCFReadStatusRepository(store.readStatuses);
        userReader = new UserReader(userRepository);
        channelReader = new ChannelReader(channelRepository);
        readStatusService = new BasicReadStatusService(readStatusRepository, userReader, channelReader);
    }

    @AfterEach
    void tearDown() {

    }

    @Nested
    @DisplayName("createReadStatus")
    class createReadStatus {
        @Test
        @DisplayName("[Integration][Positive] 읽음상태 생성 - 정상적으로 저장후 조회된다.")
        void createReadStatus_then_persists() {

            // Given
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            Channel channel = Channel.createPrivateChannel(creator.getId());
            channel.addUserId(creator.getId());
            channelRepository.save(channel);
            int before = readStatusRepository.findAll().size();

            // When
            UUID readStatusId = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            // Then
            int after = readStatusRepository.findAll().size();
            assertEquals(before + 1, after);
            ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow();
            assertEquals(creator.getId(), readStatus.getUserId());
            assertEquals(channel.getId(), readStatus.getChannelId());
            assertNotNull(readStatus.getReadAt());

        }
    }

}
