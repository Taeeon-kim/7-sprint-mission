package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.entity.type.RoleType;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import com.sprint.mission.discodeit.store.InMemoryStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class ChannelIntegrationTest {
    private final InMemoryStore store = new InMemoryStore();

    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private ReadStatusRepository readStatusRepository;
    private ChannelService channelService;
    private MessageRepository messageRepository;
    private UserReader userReader;
    private ChannelReader channelReader;
    private UserStatusRepository userStatusRepository;


    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository(store.users);
        channelRepository = new JCFChannelRepository(store.channels);
        readStatusRepository = new JCFReadStatusRepository(store.readStatuses);
        messageRepository = new JCFMessageRepository(store.messages);
        userReader = new UserReader(userRepository);
        channelReader = new ChannelReader(channelRepository);
        userStatusRepository = new JCFUserStatusRepository(store.userStatusses);

        channelService = new BasicChannelService(
                channelRepository,
                messageRepository,
                userReader,
                channelReader,
                userStatusRepository,
                readStatusRepository);
    }

    @Nested
    class CreateChannel {

        @Test
        @DisplayName("[Integration][Flow] public채널 생성 - 채널 저장 & ReadStatus 미생성")
        void createChannel_public_then_persists_channel_only() {
            // given
            User creator = userRepository.save(
                    User.builder()
                            .nickname("c")
                            .email("c@ex.com")
                            .password("pw")
                            .role(RoleType.USER)
                            .phoneNumber("010")
                            .build()
            );
            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(
                    "공지", "전체 공지", ChannelType.PUBLIC, null // memberIds 없음
            );
            int beforeChannelSize = channelRepository.findAll().size();
            int beforeReadStatusSize = readStatusRepository.findAll().size();

            // when
            channelService.createChannel(creator.getId(), channelCreateRequestDto);

            // then
            int afterChannelSize = channelRepository.findAll().size();
            int afterReadStatusSize = readStatusRepository.findAll().size();

            assertAll(
                    () -> assertEquals(beforeChannelSize + 1, afterChannelSize),
                    () -> assertEquals(beforeReadStatusSize, afterReadStatusSize) // ReadStatus 생성 X
            );

            Channel saved = channelRepository.findAll().get(afterChannelSize - 1); // 혹은 정밀조회
            assertAll(
                    () -> assertEquals(ChannelType.PUBLIC, saved.getType()),
                    () -> assertEquals("공지", saved.getTitle()),
                    () -> assertEquals("전체 공지", saved.getDescription()),
                    () -> assertEquals(creator.getId(), saved.getCreatedByUserId())
            );
        }

        @Test
        @DisplayName("[Integration][Negative] public채널 생성 - title/description 누락 시 예외")
        void createChannel_public_throws_when_invalid_fields() {
            User creator = userRepository.save(User.builder()
                    .nickname("c").email("c@ex.com").password("pw")
                    .role(RoleType.USER).phoneNumber("010").build());

            // DTO 시그니처: (title, description, type, memberIds)
            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(" ", null, ChannelType.PUBLIC, null);

            assertThrows(IllegalArgumentException.class,
                    () -> channelService.createChannel(creator.getId(), channelCreateRequestDto));
        }

        @Test
        @DisplayName("[Integration][Flow] private채널 생성 - 채널 저장 & 멤버별 ReadStatus 생성")
        void createChannel_private_then_persists_channel_and_readStatuses() {
            // given
            User creator = userRepository.save(User.builder()
                    .nickname("c").email("c@ex.com").password("pw")
                    .role(RoleType.USER).phoneNumber("010").build());

            User m1 = userRepository.save(User.builder()
                    .nickname("m1").email("m1@ex.com").password("pw")
                    .role(RoleType.USER).phoneNumber("010").build());

            User m2 = userRepository.save(User.builder()
                    .nickname("m2").email("m2@ex.com").password("pw")
                    .role(RoleType.USER).phoneNumber("010").build());

            ChannelCreateRequestDto dto = new ChannelCreateRequestDto(
                    null, null, ChannelType.PRIVATE, List.of(m1.getId(), m2.getId())
            );

            int beforeChannelSize = channelRepository.findAll().size();
            int beforeReadStatusSize = readStatusRepository.findAll().size();

            // when
            channelService.createChannel(creator.getId(), dto);

            // then
            int afterChannelSize = channelRepository.findAll().size();
            int afterReadStatusSize = readStatusRepository.findAll().size();

            assertAll(
                    () -> assertEquals(beforeChannelSize + 1, afterChannelSize),
                    () -> assertEquals(beforeReadStatusSize + 2, afterReadStatusSize) // 멤버 2명 만큼 생성
            );

            Channel channel = channelRepository.findAll().get(afterChannelSize - 1);
            assertAll(
                    () -> assertEquals(ChannelType.PRIVATE, channel.getType()),
                    () -> assertNull(channel.getTitle()),
                    () -> assertNull(channel.getDescription()),
                    () -> assertEquals(creator.getId(), channel.getCreatedByUserId())
            );

            // ReadStatus 검증
            // (findByChannelId가 없다면 findAll().stream()으로 대체)
            List<ReadStatus> statuses = readStatusRepository.findByChannelId(channel.getId());
            Set<UUID> userIds = statuses.stream().map(ReadStatus::getUserId).collect(Collectors.toSet());

            assertEquals(Set.of(m1.getId(), m2.getId()), userIds);

            Instant now = Instant.now();
            statuses.forEach(readStatus -> {
                assertNotNull(readStatus.getReadAt());
                assertTrue(readStatus.getReadAt().isBefore(now.plusSeconds(5)));
                assertTrue(readStatus.getReadAt().isAfter(now.minusSeconds(30)));
            });
        }

        @Test
        @DisplayName("[Integration][Negative] private채널 생성 - 존재하지 않는 멤버 포함 시 예외")
        void createChannel_private_throws_when_member_not_found() {
            User creator = userRepository.save(User.builder()
                    .nickname("c").email("c@ex.com").password("pw")
                    .role(RoleType.USER).phoneNumber("010").build());

            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(
                    null, null, ChannelType.PRIVATE, List.of(UUID.randomUUID()) // 존재하지 않는 유저
            );

            assertThrows(NoSuchElementException.class,
                    () -> channelService.createChannel(creator.getId(), channelCreateRequestDto)); // userReader.findUserOrThrow(memberId)에서 터짐
        }

    }

    @Nested
    @DisplayName("getChannel")
    class getChannel {


        @Test
        @DisplayName("[Integration][Positive] 채널 조회 - 기본 채널 정보와 최신 메세지 시간 반환 ")
        void getChannel_then_returns_result() throws InterruptedException {

            // given
            User creator = userRepository.save(
                    User.builder()
                            .nickname("creator")
                            .email("c@ex.com")
                            .password("pw")
                            .role(RoleType.USER)
                            .phoneNumber("010")
                            .build()
            );

            Channel channel = channelRepository.save(
                    Channel.createPublicChannel("공지", "전체 공지", creator.getId())
            );

            // 메시지 두 개 저장 (시간 차이 확인용)
            Message m1 = messageRepository.save(
                    new Message("hello1", channel.getId(), creator.getId(), null)
            );
            channel.addMessageId(m1.getId());
            sleep(10); // 10ms 정도 차이 — LocalDateTime/Instant 비교 안전
            Message m2 = messageRepository.save(
                    new Message("hello2", channel.getId(), creator.getId(), null)
            );
            channel.addMessageId(m2.getId());

            // when
            ChannelResponseDto result = channelService.getChannel(channel.getId());

            // then
            assertAll(
                    () -> assertEquals(channel.getId(), result.channelId()),
                    () -> assertEquals("공지", result.title()),
                    () -> assertEquals("전체 공지", result.description()),
                    () -> assertEquals(ChannelType.PUBLIC, result.type()),
                    () -> assertEquals(creator.getId(), result.createdByUserId()),
                    // 최신 메시지 시간 검증
                    () -> assertNotNull(result.currentMessagedAt()),
                    () -> assertEquals(m2.getCreatedAt(), result.currentMessagedAt())
            );
        }

        @Test
        @DisplayName("[Integration][Positive] 채널 조회 - 존재하지않는 채널 조회시 예외")
        void getChannel_throws_when_not_found() {
            UUID id = UUID.randomUUID();
            assertThrows(NoSuchElementException.class, () -> channelService.getChannel(id));
        }

        @Test
        @DisplayName("[Integration][Negative] 채널 조회 - 채널에 등록된 messageId가 저장소에 없다면 currentMessagedAt 이 null ")
        void getChannel_null_when_message_not_found() {
            // given
            User creator = userRepository.save(
                    User.builder()
                            .nickname("creator")
                            .email("c@ex.com")
                            .password("pw")
                            .role(RoleType.USER)
                            .phoneNumber("010")
                            .build()
            );

            Channel channel = channelRepository.save(
                    Channel.createPublicChannel("공지", "전체 공지", creator.getId())
            );

            // 메세지만 생성
            Message m1 = new Message("hello1", channel.getId(), creator.getId(), null);

            // 메세지 레포지토리에 넣지않고 채널에 추가
            channel.addMessageId(m1.getId());

            // when
            ChannelResponseDto responsedChannel = channelService.getChannel(channel.getId());

            //  then
            assertAll(
                    () -> assertEquals(channel.getId(), responsedChannel.channelId()),
                    () -> assertEquals("공지", responsedChannel.title()),
                    () -> assertEquals("전체 공지", responsedChannel.description()),
                    () -> assertEquals(ChannelType.PUBLIC, responsedChannel.type()),
                    () -> assertEquals(creator.getId(), responsedChannel.createdByUserId()),
                    () -> assertNull(responsedChannel.currentMessagedAt()) // 핵심 검증
            );
        }

        @Test
        @DisplayName("[Integration][Edge] 채널 조회 - 메시지가 하나도 없으면 currentMessagedAt == null")
        void getChannel_null_when_no_messages() {
            // given
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );
            Channel channel = channelRepository.save(
                    Channel.createPublicChannel("공지", "전체 공지", creator.getId())
            );

            // when
            ChannelResponseDto dto = channelService.getChannel(channel.getId());

            // then
            assertNull(dto.currentMessagedAt());
        }

    }
}
