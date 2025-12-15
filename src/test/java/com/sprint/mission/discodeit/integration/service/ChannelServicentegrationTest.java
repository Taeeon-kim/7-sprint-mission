package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.channel.*;
import com.sprint.mission.discodeit.integration.fixtures.ChannelFixture;
import com.sprint.mission.discodeit.integration.fixtures.MessageFixture;
import com.sprint.mission.discodeit.integration.fixtures.ReadStatusFixture;
import com.sprint.mission.discodeit.integration.fixtures.UserFixture;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChannelServicentegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;


//    @BeforeEach
//    void setUp() {
//        jdbcTemplate.execute("TRUNCATE TABLE read_statuses, messages, channels, users, user_statuses RESTART IDENTITY CASCADE");
//    }

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
                            .build()
            );
            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(
                    "공지", "전체 공지", null // memberIds 없음
            );

            ChannelCreateCommand cmd = ChannelCreateCommand.from(channelCreateRequestDto, ChannelType.PUBLIC);
            int beforeChannelSize = channelRepository.findAll().size();
            int beforeReadStatusSize = readStatusRepository.findAll().size();

            // when
            channelService.createChannel(cmd);

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
                    () -> assertEquals("공지", saved.getName()),
                    () -> assertEquals("전체 공지", saved.getDescription())
            );
        }

        @Test
        @DisplayName("[Integration][Negative] public 채널 생성 - public 채널에 readStatus 생성 시도시 예외 발생")
        void createChannel_public_throws_when_try_create_read_status() throws InterruptedException {
            // given
            User user = UserFixture.createUserWithStatus(userRepository);
            Channel publicChannel = ChannelFixture.createPublicChannel(channelRepository);
            assertThrows(IllegalArgumentException.class, () ->
                    ReadStatusFixture.joinChannel(user, publicChannel, readStatusRepository)
            );
        }

        @Test
        @DisplayName("[Integration][Negative] private 채널 생성 - 참여자가 2명이상이 아닐시 예외 발생")
        void updateMessage_updates_content_when_edits() {
            // given
            User user = UserFixture.createUserWithStatus(userRepository);

            //when & then
            assertThrows(ChannelMinimumMembersNotMetException.class, () ->
                    channelService.createChannel(ChannelCreateCommand.from(
                            new ChannelCreateRequestDto(null, null, List.of(user.getId())),
                            ChannelType.PRIVATE
                    ))
            );
        }

        @Test
        @DisplayName("[Integration][Negative] public채널 생성 - title/description 누락 시 예외")
        void createChannel_public_throws_when_invalid_fields() {
            User creator = userRepository.save(User.builder()
                    .nickname("c").email("c@ex.com").password("pw")
                    .build());

            // DTO 시그니처: (title, description, type, memberIds)
            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(" ", null, null);
            ChannelCreateCommand cmd = ChannelCreateCommand.from(channelCreateRequestDto, ChannelType.PUBLIC);
            assertThrows(IllegalArgumentException.class,
                    () -> channelService.createChannel(cmd));
        }

        @Test
        @DisplayName("[Integration][Flow] private채널 생성 - 채널 저장 & 멤버별 ReadStatus 생성")
        void createChannel_private_then_persists_channel_and_readStatuses() {
            // given
            User creator = userRepository.save(User.builder()
                    .nickname("c").email("c@ex.com").password("pw")
                    .build());

            User m1 = userRepository.save(User.builder()
                    .nickname("m1").email("m1@ex.com").password("pw")
                    .build());

            User m2 = userRepository.save(User.builder()
                    .nickname("m2").email("m2@ex.com").password("pw")
                    .build());

            ChannelCreateRequestDto dto = new ChannelCreateRequestDto(
                    null, null, List.of(m1.getId(), m2.getId())
            );
            ChannelCreateCommand cmd = ChannelCreateCommand.from(dto, ChannelType.PRIVATE);
            int beforeChannelSize = channelRepository.findAll().size();
            int beforeReadStatusSize = readStatusRepository.findAll().size();

            // when
            channelService.createChannel(cmd);

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
                    () -> assertNull(channel.getName()),
                    () -> assertNull(channel.getDescription())
            );

            // ReadStatus 검증
            // (findByChannelId가 없다면 findAll().stream()으로 대체)
            List<ReadStatus> statuses = readStatusRepository.findByChannelId(channel.getId());
            Set<UUID> userIds = statuses.stream()
                    .map(readStatus -> readStatus.getUser().getId())
                    .collect(Collectors.toSet());

            assertEquals(Set.of(m1.getId(), m2.getId()), userIds);

            Instant now = Instant.now();
            statuses.forEach(readStatus -> {
                assertNotNull(readStatus.getLastReadAt());
                assertTrue(readStatus.getLastReadAt().isBefore(now.plusSeconds(5)));
                assertTrue(readStatus.getLastReadAt().isAfter(now.minusSeconds(30)));
            });
        }

        @Test
        @DisplayName("[Integration][Negative] private채널 생성 - 존재하지 않는 멤버로 private 채널 생성시 인원불충족으로 예외")
        void createChannel_private_throws_when_member_not_found() {
//            User creator = userRepository.save(User.builder()
//                    .nickname("c").email("c@ex.com").password("pw")
//                    .build());

            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(
                    null, null, List.of(UUID.randomUUID()) // 존재하지 않는 유저
            );

            ChannelCreateCommand cmd = ChannelCreateCommand.from(channelCreateRequestDto, ChannelType.PRIVATE);

            assertThrows(ChannelMinimumMembersNotMetException.class,
                    () -> channelService.createChannel(cmd));
        }

    }

    @Nested
    @DisplayName("getChannel")
    class getChannel {


        @Test
        @DisplayName("[Integration][Positive] 채널 조회 - 기본 채널 정보와 최신 메세지 시간 반환 ")
        void getChannel_then_returns_result() throws InterruptedException {

            // given
            User creator = UserFixture.createUserWithStatus(userRepository);

            Channel channel = ChannelFixture.createPublicChannel("공지", "전체 공지", channelRepository);

            // 메시지 두 개 저장 (시간 차이 확인용)
            Message m1 = MessageFixture.sendMessage("hello1", creator, channel, null, messageRepository);

            sleep(10); // 10ms 정도 차이 — LocalDateTime/Instant 비교 안전
            Message m2 = MessageFixture.sendMessage("hello2", creator, channel, null, messageRepository);

            // when
            ChannelResponseDto result = channelService.getChannel(channel.getId());

            // then
            assertAll(
                    () -> assertEquals(channel.getId(), result.id()),
                    () -> assertEquals("공지", result.name()),
                    () -> assertEquals("전체 공지", result.description()),
                    () -> assertEquals(ChannelType.PUBLIC, result.type()),
                    // 최신 메시지 시간 검증
                    () -> assertNotNull(result.lastMessageAt()),
                    () -> assertEquals(m2.getCreatedAt(), result.lastMessageAt())
            );
        }

        @Test
        @DisplayName("[Integration][Positive] 채널 조회 - 존재하지않는 채널 조회시 예외")
        void getChannel_throws_when_not_found() {
            UUID id = UUID.randomUUID();
            assertThrows(ChannelNotFoundException.class, () -> channelService.getChannel(id));
        }

        @Test
        @DisplayName("[Integration][Negative] 채널 조회 - 채널에 메세지가 없으면 lastMessageAt은 null 이다")
        void getChannel_null_when_message_not_found() {
            // given
            User creator = UserFixture.createUserWithStatus(userRepository);

            Channel channel = ChannelFixture.createPublicChannel("공지", "전체 공지", channelRepository);

            // when
            ChannelResponseDto responsedChannel = channelService.getChannel(channel.getId());

            //  then
            assertAll(
                    () -> assertEquals(channel.getId(), responsedChannel.id()),
                    () -> assertEquals("공지", responsedChannel.name()),
                    () -> assertEquals("전체 공지", responsedChannel.description()),
                    () -> assertEquals(ChannelType.PUBLIC, responsedChannel.type()),
                    () -> assertNull(responsedChannel.lastMessageAt()) // 핵심 검증
            );
        }

        @Test
        @DisplayName("[Integration][Edge] 채널 조회 - 메시지가 하나도 없으면 currentMessagedAt == null")
        void getChannel_null_when_no_messages() {
            // given
            User creator = UserFixture.createUserWithStatus(userRepository);

            Channel channel = ChannelFixture.createPublicChannel("공지", "전체 공지", channelRepository);

            // when
            ChannelResponseDto dto = channelService.getChannel(channel.getId());

            // then
            assertNull(dto.lastMessageAt());
        }

    }

    @Nested
    @DisplayName("getAllChannel")
    class getAllChannel {

        @Test
        @DisplayName("[Integration][Positive] 전체 채널 조회 - 각 채널 정보와 최신 메시지 시간 포함")
        void getAllChannels_returns_channel_list_with_last_message_time() throws InterruptedException {

            // given
            User creator = UserFixture.createUserWithStatus(userRepository);

            Channel channelA = ChannelFixture.createPublicChannel("공지", "전체 공지", channelRepository);

            Message messageA1 = MessageFixture.sendMessage("A1", creator, channelA, null, messageRepository);
            Thread.sleep(5);
            Message messageA2 = MessageFixture.sendMessage("A2", creator, channelA, null, messageRepository);

            // Channel B
            Channel channelB = ChannelFixture.createPublicChannel("잡담", "자유 채팅방", channelRepository);

            Message messageB1 = MessageFixture.sendMessage("B1", creator, channelB, null, messageRepository);

            Channel channelC = ChannelFixture.createPrivateChannel(channelRepository);

            // when
            List<ChannelResponseDto> result = channelService.getAllChannels();

            // then
            assertEquals(3, result.size());

            // 채널별 DTO 찾기
            ChannelResponseDto dtoA = result.stream()
                    .filter(r -> r.id().equals(channelA.getId()))
                    .findFirst()
                    .orElseThrow();

            ChannelResponseDto dtoB = result.stream()
                    .filter(r -> r.id().equals(channelB.getId()))
                    .findFirst()
                    .orElseThrow();

            assertAll(
                    // Channel A
                    () -> assertEquals("공지", dtoA.name()),
                    () -> assertEquals("전체 공지", dtoA.description()),
                    () -> assertEquals(messageA2.getCreatedAt(), dtoA.lastMessageAt()),

                    // Channel B
                    () -> assertEquals("잡담", dtoB.name()),
                    () -> assertEquals("자유 채팅방", dtoB.description()),
                    () -> assertEquals(messageB1.getCreatedAt(), dtoB.lastMessageAt())
            );
        }
    }

    @Nested
    @DisplayName("getAllChannelsByUserId")
    class getAllChannelsByUserId {

        @Test
        @DisplayName("[Integration][Positive] 유저채널 조회 - Public 채널 및 자기속한 private 채널 조회")
        void getAllChannelsByUserId_returns_public_and_joined_private_channel_list() {
            // given
            User creator = UserFixture.createUserWithStatus(userRepository);

            Channel channelA = channelRepository.save(
                    Channel.createPublicChannel("공지", "전체 공지")
            );
            Channel channelB = channelRepository.save(
                    Channel.createPublicChannel("자유", "자유 게시판채널")
            );
            Channel channelC = channelRepository.save(
                    Channel.createPrivateChannel()
            );
            Channel channelD = channelRepository.save(
                    Channel.createPrivateChannel()
            );

            // joined member

            User member1 = User.builder()
                    .nickname("member1")
                    .email("aaaaac@ex.com")
                    .password("p111w")
                    .build();
            User savedUser = UserFixture.createUserWithStatus(member1, userRepository);

            User member2 = User.builder()
                    .nickname("member2")
                    .email("xxxx@ex.com")
                    .password("xxx")
                    .build();

            User savedUser2 = UserFixture.createUserWithStatus(member2, userRepository);

            ReadStatusFixture.joinChannel(member1, channelC, readStatusRepository);

            // when
            List<ChannelResponseDto> result = channelService.getAllChannelsByUserId(member1.getId());


            // then
            // 포함/제외 채널 id 세트 검증 (순서 무시)
            Set<UUID> actualIds = result.stream()
                    .map(ChannelResponseDto::id)
                    .collect(Collectors.toSet());

            assertAll(
                    // 개수 검증
                    () -> assertEquals(3, result.size()),

                    // 공개 + 가입된 private만 포함
                    () -> assertEquals(
                            Set.of(channelA.getId(), channelB.getId(), channelC.getId()),
                            actualIds,
                            "A,B(공개) + C(가입된 비공개)만 포함되어야 함"
                    ),

                    // D는 포함되면 안 됨
                    () -> assertFalse(actualIds.contains(channelD.getId()), "D(미가입 비공개)는 보이면 안 됨"),

                    // userIds 정책 검증
                    () -> result.forEach(dto -> {
                        if (dto.id().equals(channelC.getId())) {
                            assertNotNull(dto.participants(), "비공개 채널은 참여자 정보 제공");
                        } else {
                            assertTrue(dto.participants().isEmpty(), "공개 채널은 맴버가 없어야한다");
                        }
                    })
            );
        }

    }

    @Nested
    @DisplayName("updateChannel")
    class updateChannel {
        @Autowired
        EntityManager em;

        @Test
        @DisplayName("[Integration][Positive] 채널 변경 - Public 채널 수정시 내용 반영된다")
        void updateChannel_updates_public_channel_fields() {
            // given
            User creator = UserFixture.createUserWithStatus(userRepository);
            Channel channel = ChannelFixture.createPublicChannel(channelRepository);


            ChannelUpdateRequestDto request = ChannelUpdateRequestDto.builder()
                    .newName("수정한 타이틀")
                    .newDescription("수정한 설명란")
                    .build();


            // 변경 전 스냅샷
            UUID channelId = channel.getId();
            Instant beforeUpdatedAt = channel.getUpdatedAt();
            ChannelType beforeType = channel.getType();
            Instant beforeCreatedAt = channel.getCreatedAt();

            // when
            channelService.updateChannel(channel.getId(), request);
            em.flush();
            em.clear();

            // then
            Channel updatedChannel = channelRepository.findById(channel.getId()).orElseThrow();
            assertAll(
                    // 식별자/기본 속성 유지
                    () -> assertEquals(channel.getId(), updatedChannel.getId()),
                    () -> assertEquals(channelId, updatedChannel.getId()),
                    () -> assertEquals(beforeType, updatedChannel.getType()),
                    () -> assertEquals(beforeCreatedAt, updatedChannel.getCreatedAt()),

                    // 변경된 필드
                    () -> assertEquals("수정한 타이틀", updatedChannel.getName()),
                    () -> assertEquals("수정한 설명란", updatedChannel.getDescription()),

                    // updatedAt 갱신 확인(널 아님 + 이전보다 늦음)
                    () -> assertNotNull(updatedChannel.getUpdatedAt()),
                    () -> assertTrue(updatedChannel.getUpdatedAt().isAfter(beforeUpdatedAt),
                            "updatedAt은 이전 값보다 이후여야 한다")
            );
        }

        @Test
        @DisplayName("[Integration][Negative] 채널 변경 - Private 채널 수정시 예외가 발생한다.")
        void updateChannel_throws_when_update_private_channel() {
            // given
            User creator = UserFixture.createUserWithStatus(userRepository);
            Channel privateChannel = ChannelFixture.createPrivateChannel(channelRepository);

            ChannelUpdateRequestDto request = ChannelUpdateRequestDto.builder()
                    .newName("수정한 타이틀")
                    .newDescription("수정한 설명란")
                    .build();
            // when & then
            assertThrows(ChannelModificationNotAllowedException.class, () -> channelService.updateChannel(privateChannel.getId(), request));
        }
    }

    @Nested
    @DisplayName("deleteChannel")
    class deleteChannel {

        @Test
        @DisplayName("[Integration][Positive] 채널 삭제 - 채널/메시지/읽음상태 모두 삭제된다 ")
        void deleteChannel_deletes_channel_and_related_messages_read_statuses() {
            // given

            User member1 = UserFixture.createUserWithStatus(User.builder().nickname("member1").email("csdd@ex.com")
                    .password("pssw").build(), userRepository);
            User member2 = UserFixture.createUserWithStatus(User.builder().nickname("member2").email("cfffsdd@ex.com")
                    .password("pssssw").build(), userRepository);

            ChannelCreateRequestDto dto = ChannelCreateRequestDto.builder()
                    .name(null)
                    .description(null)
                    .participantIds(List.of(member1.getId(), member2.getId()))
                    .build();

            ChannelCreateCommand cmd = ChannelCreateCommand.from(dto, ChannelType.PRIVATE);

            ChannelResponseDto response = channelService.createChannel(cmd);// TODO: 추후 service 의존도 빼고 repository로 만들것

            // 방금 생긴 채널 식별 (가장 최근/사이즈 차이로 추적)
            Channel channel = channelRepository.findById(response.id()).orElseThrow();
            // 메시지 2개 저장 + 채널에 messageId 연결 후 채널 재저장
            Message m1 = MessageFixture.sendMessage("hello1", member1, channel, null, messageRepository);
            Message m2 = MessageFixture.sendMessage("hello2", member1, channel, null, messageRepository);

            // then
            assertAll(
                    () -> assertTrue(messageRepository.findById(m1.getId()).isPresent()),
                    () -> assertTrue(messageRepository.findById(m2.getId()).isPresent()),
                    () -> assertFalse(readStatusRepository.findByChannelId(channel.getId()).isEmpty())
            );

            // when
            channelService.deleteChannel(channel.getId());

            // then
            assertAll(
                    // 채널 삭제
                    () -> assertTrue(channelRepository.findById(channel.getId()).isEmpty()),
                    // 메시지 삭제
                    () -> assertTrue(messageRepository.findById(m1.getId()).isEmpty()),
                    () -> assertTrue(messageRepository.findById(m2.getId()).isEmpty()),
                    // ReadStatus 삭제
                    () -> assertTrue(readStatusRepository.findByChannelId(channel.getId()).isEmpty())
            );

        }
    }

}

