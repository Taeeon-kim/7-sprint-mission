package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
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
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ReadStatusServiceIntegrationTest {

    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private ReadStatusRepository readStatusRepository;
    private ReadStatusService readStatusService;
    private UserReader userReader;
    private ChannelReader channelReader;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        channelRepository = new JCFChannelRepository();
        readStatusRepository = new JCFReadStatusRepository();
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

    @Nested
    @DisplayName("getReadStatus")
    class getReadStatus {

        @Test
        @DisplayName("[Integration][Positive] 읽음상태 조회 - 해당 읽음상태 반환 및 값 일치")
        void getReadStatus_returns_saved_readStatus() {
            // Given
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            Channel channel = Channel.createPrivateChannel(creator.getId());
            channel.addUserId(creator.getId());
            channelRepository.save(channel);


            UUID readStatusId = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            // when
            ReadStatusResponseDto readStatus = readStatusService.getReadStatus(readStatusId);

            // then
            assertEquals(creator.getId(), readStatus.userId());
            assertEquals(channel.getId(), readStatus.channelId());
            assertNotNull(readStatus.readAt());

        }

        @Test
        @DisplayName("[Integration][Negative] 읽음상태 조회 - 존재하지않는 Id 조회시 NoSuchElementException 예외")
        void getReadStatus_throws_when_not_found() {

            UUID readStatusId = UUID.randomUUID();

            assertThrows(NoSuchElementException.class, () -> readStatusService.getReadStatus(readStatusId));

        }


    }

    @Nested
    @DisplayName("getAllReadStatusesByUserId")
    class getAllReadStatusesByUserId {

        @Test
        @DisplayName("[Integration][Positive] 다중 읽음상태 조회 - 리스트로 반환 및 값 일치")
        void getAllReadStatusesByUserId_returns_list_of_readStatus() {

            // Given
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            User member = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            Channel channel = Channel.createPrivateChannel(creator.getId());
            channel.addUserId(creator.getId());
            Channel savedChannel = channelRepository.save(channel);

            Channel channel2 = Channel.createPrivateChannel(creator.getId());
            Channel savedChannel2 = channelRepository.save(channel2);


            UUID readStatusId = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );


            UUID readStatusId2 = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel2.getId()).build()
            );


            UUID readStatusId3 = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(member.getId())
                            .channelId(channel.getId()).build()
            );

            UUID readStatusId4 = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(member.getId())
                            .channelId(channel2.getId()).build()
            );


            // when
            List<ReadStatusResponseDto> allReadStatusesByUserId = readStatusService.getAllReadStatusesByUserId(creator.getId());
            List<ReadStatusResponseDto> allReadStatusesByUserId2 = readStatusService.getAllReadStatusesByUserId(member.getId());
            // then
            Set<UUID> readStatusIds = allReadStatusesByUserId.stream()
                    .map(ReadStatusResponseDto::id).collect(Collectors.toSet());
            Set<UUID> readStatusIds2 = allReadStatusesByUserId2.stream()
                    .map(ReadStatusResponseDto::id).collect(Collectors.toSet());

            assertAll(
                    () -> assertEquals(2, allReadStatusesByUserId.size()),
                    () -> assertEquals(Set.of(readStatusId, readStatusId2), readStatusIds),
                    () -> assertTrue(allReadStatusesByUserId.stream().allMatch(readStatus -> readStatus.userId().equals(creator.getId()))),
                    () -> assertEquals(2, allReadStatusesByUserId2.size()),
                    () -> assertEquals(Set.of(readStatusId3, readStatusId4), readStatusIds2),
                    () -> assertTrue(allReadStatusesByUserId2.stream().allMatch(readStatus -> readStatus.userId().equals(member.getId())))
            );

        }
    }

    @Nested
    @DisplayName("updateReadStatus")
    class updateReadStatus {

        @Test
        @DisplayName("[Integration][Positive] 읽음상태 수정 - 변경값 비교 일치")
        void updateReadStatus_then_changed_values() {
            // given
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            User member = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            Channel channel = Channel.createPrivateChannel(creator.getId());
            channel.addUserId(creator.getId());
            Channel savedChannel = channelRepository.save(channel);

            UUID readStatusId = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            // when
            Instant expectedReadAt = Instant.ofEpochMilli(1000L);
            readStatusService.updateReadStatus(
                    readStatusId,
                    ReadStatusUpdateRequestDto.builder()
                            .readAt(expectedReadAt)
                            .build()
            );

            // then
            Instant readAt = readStatusRepository.findById(readStatusId).orElseThrow().getReadAt();
              assertEquals(expectedReadAt, readAt);

        }
        //TODO: 예외 추가(isBofre 이아닌 미래값)
    }

    @Nested
    @DisplayName("deleteReadStatus")
    class deleteReadStatus {


        @Test
        @DisplayName("[Integration][Positive] 읽음상태 삭제 - 삭제 후 조회 불가")
        void deleteReadStatus_then_not_found() {
            // given
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            User member = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            Channel channel = Channel.createPrivateChannel(creator.getId());
            channel.addUserId(creator.getId());
            Channel savedChannel = channelRepository.save(channel);

            UUID readStatusId = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            // when
            readStatusService.deleteReadStatus(readStatusId);

            // then
            assertThrows(NoSuchElementException.class, () -> readStatusService.getReadStatus(readStatusId));


        }

    }

}
