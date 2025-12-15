package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateCommand;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.integration.fixtures.ChannelFixture;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReadStatusServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private ReadStatusService readStatusService;

    @BeforeEach
    void setUp() {

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
                            .password("pw").build()
            );

            Channel channel = Channel.createPrivateChannel();
            channelRepository.save(channel);
            int before = readStatusRepository.findAll().size();

            // When
            ReadStatusResponseDto responseDto = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            // Then
            int after = readStatusRepository.findAll().size();
            assertEquals(before + 1, after);
            ReadStatus readStatus = readStatusRepository.findById(responseDto.id()).orElseThrow();
            assertEquals(creator.getId(), readStatus.getUser().getId());
            assertEquals(channel.getId(), readStatus.getChannel().getId());
            assertNotNull(readStatus.getLastReadAt());

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
                            .password("pw").build()
            );

            Channel channel = Channel.createPrivateChannel();
            channelRepository.save(channel);


            ReadStatusResponseDto responseDto = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            // when
            ReadStatusResponseDto readStatus = readStatusService.getReadStatus(responseDto.id());

            // then
            assertEquals(creator.getId(), readStatus.userId());
            assertEquals(channel.getId(), readStatus.channelId());
            assertNotNull(readStatus.lastReadAt());

        }

        @Test
        @DisplayName("[Integration][Negative] 읽음상태 조회 - 존재하지않는 Id 조회시 NoSuchElementException 예외")
        void getReadStatus_throws_when_not_found() {

            UUID readStatusId = UUID.randomUUID();

            assertThrows(ReadStatusNotFoundException.class, () -> readStatusService.getReadStatus(readStatusId));

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
                            .password("pw").build()
            );

            User member = userRepository.save(
                    User.builder().nickname("creator2").email("c2@ex.com")
                            .password("pw").build()
            );

            Channel channel = ChannelFixture.createPrivateChannel(channelRepository);

            Channel channel2 = ChannelFixture.createPrivateChannel(channelRepository);

            ReadStatusResponseDto responseDto = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            ReadStatusResponseDto responseDto2 = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel2.getId()).build()
            );

            ReadStatusResponseDto responseDto3 = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(member.getId())
                            .channelId(channel.getId()).build()
            );

            ReadStatusResponseDto responseDto4 = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(member.getId())
                            .channelId(channel2.getId()).build()
            );


            // when
            List<ReadStatusResponseDto> allReadStatusesByUserId = readStatusService.getAllReadStatusesByUserId(creator.getId());
            System.out.println("allReadStatusesByUserId = " + allReadStatusesByUserId.get(0).userId());
            List<ReadStatusResponseDto> allReadStatusesByUserId2 = readStatusService.getAllReadStatusesByUserId(member.getId());
            System.out.println("allReadStatusesByUserId2 = " + allReadStatusesByUserId2);
            // then
            Set<UUID> readStatusIds = allReadStatusesByUserId.stream()
                    .map(ReadStatusResponseDto::id).collect(Collectors.toSet());
            Set<UUID> readStatusIds2 = allReadStatusesByUserId2.stream()
                    .map(ReadStatusResponseDto::id).collect(Collectors.toSet());

            assertAll(
                    () -> assertEquals(2, allReadStatusesByUserId.size()),
                    () -> assertEquals(Set.of(responseDto.id(), responseDto2.id()), readStatusIds),
                    () -> assertTrue(allReadStatusesByUserId.stream().allMatch(readStatus -> readStatus.userId().equals(creator.getId()))),
                    () -> assertEquals(2, allReadStatusesByUserId2.size()),
                    () -> assertEquals(Set.of(responseDto3.id(), responseDto4.id()), readStatusIds2),
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
                            .password("pw").build()
            );

            Channel channel = ChannelFixture.createPrivateChannel(channelRepository);

            ReadStatusResponseDto responseDto = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            // when
            Instant expectedReadAt = Instant.now();
            readStatusService.updateReadStatus(
                    ReadStatusUpdateCommand.from(responseDto.id(),
                            ReadStatusUpdateRequestDto.builder()
                                    .newLastReadAt(expectedReadAt)
                                    .build())
            );

            // then
            Instant readAt = readStatusRepository.findById(responseDto.id()).orElseThrow().getLastReadAt();
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
                            .password("pw").build()
            );


            Channel channel = ChannelFixture.createPrivateChannel(channelRepository);

            ReadStatusResponseDto responseDto = readStatusService.createReadStatus(
                    ReadStatusCreateRequestDto.builder()
                            .userId(creator.getId())
                            .channelId(channel.getId()).build()
            );

            // when
            readStatusService.deleteReadStatus(responseDto.id());

            // then
            assertThrows(ReadStatusNotFoundException.class, () -> readStatusService.getReadStatus(responseDto.id()));


        }

    }

}
