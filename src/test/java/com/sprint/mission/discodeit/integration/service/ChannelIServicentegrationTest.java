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
import com.sprint.mission.discodeit.entity.type.RoleType;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
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

public class ChannelIServicentegrationTest {
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
        userRepository = new JCFUserRepository();
        channelRepository = new JCFChannelRepository();
        readStatusRepository = new JCFReadStatusRepository();
        messageRepository = new JCFMessageRepository();
        userReader = new UserReader(userRepository);
        channelReader = new ChannelReader(channelRepository);
        userStatusRepository = new JCFUserStatusRepository();

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
            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(creator.getId(),
                    "공지", "전체 공지", ChannelType.PUBLIC, null // memberIds 없음
            );

            ChannelCreateCommand cmd = ChannelCreateCommand.from(channelCreateRequestDto);
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
            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(creator.getId(), " ", null, ChannelType.PUBLIC, null);
            ChannelCreateCommand cmd = ChannelCreateCommand.from(channelCreateRequestDto);
            assertThrows(IllegalArgumentException.class,
                    () -> channelService.createChannel(cmd));
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

            ChannelCreateRequestDto dto = new ChannelCreateRequestDto(creator.getId(),
                    null, null, ChannelType.PRIVATE, List.of(m1.getId(), m2.getId())
            );
            ChannelCreateCommand cmd = ChannelCreateCommand.from(dto);
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

            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(creator.getId(),
                    null, null, ChannelType.PRIVATE, List.of(UUID.randomUUID()) // 존재하지 않는 유저
            );

            ChannelCreateCommand cmd = ChannelCreateCommand.from(channelCreateRequestDto);

            assertThrows(NoSuchElementException.class,
                    () -> channelService.createChannel(cmd)); // userReader.findUserOrThrow(memberId)에서 터짐
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
                    Channel.createPublicChannel(creator.getId(), "공지", "전체 공지")
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

            channelRepository.save(channel);

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
                    Channel.createPublicChannel(creator.getId(), "공지", "전체 공지")
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
                    Channel.createPublicChannel(creator.getId(), "공지", "전체 공지")
            );

            // when
            ChannelResponseDto dto = channelService.getChannel(channel.getId());

            // then
            assertNull(dto.currentMessagedAt());
        }

    }

    @Nested
    @DisplayName("getAllChannel")
    class getAllChannel {

        @Test
        @DisplayName("[Integration][Positive] 전체 채널 조회 - 각 채널 정보와 최신 메시지 시간 포함")
        void getAllChannels_returns_channel_list_with_last_message_time() throws InterruptedException {

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

            // Channel A
            Channel channelA = channelRepository.save(
                    Channel.createPublicChannel(creator.getId(), "공지", "전체 공지")
            );
            Message messageA1 = messageRepository.save(new Message("A1", channelA.getId(), creator.getId(), null));
            channelA.addMessageId(messageA1.getId());
            Thread.sleep(5);
            Message messageA2 = messageRepository.save(new Message("A2", channelA.getId(), creator.getId(), null));
            channelA.addMessageId(messageA2.getId());
            channelRepository.save(channelA);

            // Channel B
            Channel channelB = channelRepository.save(
                    Channel.createPublicChannel(creator.getId(), "잡담", "자유 채팅방")
            );
            Message messageB1 = messageRepository.save(new Message("B1", channelB.getId(), creator.getId(), null));
            channelB.addMessageId(messageB1.getId());
            channelRepository.save(channelB);


            Channel channelC = channelRepository.save(
                    Channel.createPrivateChannel(creator.getId())
            );
            channelRepository.save(channelB);

            // when
            List<ChannelResponseDto> result = channelService.getAllChannels();

            // then
            assertEquals(3, result.size());

            // 채널별 DTO 찾기
            ChannelResponseDto dtoA = result.stream()
                    .filter(r -> r.channelId().equals(channelA.getId()))
                    .findFirst()
                    .orElseThrow();

            ChannelResponseDto dtoB = result.stream()
                    .filter(r -> r.channelId().equals(channelB.getId()))
                    .findFirst()
                    .orElseThrow();

            assertAll(
                    // Channel A
                    () -> assertEquals("공지", dtoA.title()),
                    () -> assertEquals("전체 공지", dtoA.description()),
                    () -> assertEquals(messageA2.getCreatedAt(), dtoA.currentMessagedAt()),

                    // Channel B
                    () -> assertEquals("잡담", dtoB.title()),
                    () -> assertEquals("자유 채팅방", dtoB.description()),
                    () -> assertEquals(messageB1.getCreatedAt(), dtoB.currentMessagedAt())
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
            User creator = userRepository.save(
                    User.builder()
                            .nickname("creator")
                            .email("c@ex.com")
                            .password("pw")
                            .role(RoleType.USER)
                            .phoneNumber("010")
                            .build()
            );

            Channel channelA = channelRepository.save(
                    Channel.createPublicChannel(creator.getId(), "공지", "전체 공지")
            );
            Channel channelB = channelRepository.save(
                    Channel.createPublicChannel(creator.getId(), "자유", "자유 게시판채널")
            );
            Channel channelC = channelRepository.save(
                    Channel.createPrivateChannel(creator.getId())
            );
            Channel channelD = channelRepository.save(
                    Channel.createPrivateChannel(creator.getId())
            );

            // joined member
            User member1 = userRepository.save(User.builder()
                    .nickname("member1")
                    .email("aaaaac@ex.com")
                    .password("p111w")
                    .role(RoleType.USER)
                    .phoneNumber("010-1111-2222")
                    .build());

            User member2 = userRepository.save(User.builder()
                    .nickname("member2")
                    .email("xxxx@ex.com")
                    .password("xxx")
                    .role(RoleType.USER)
                    .phoneNumber("010-3333-4444")
                    .build());


            channelC.addUserId(member1.getId());
            channelRepository.save(channelC);


            // when
            List<ChannelResponseDto> result = channelService.getAllChannelsByUserId(member1.getId());


            // then
            // 포함/제외 채널 id 세트 검증 (순서 무시)
            Set<UUID> actualIds = result.stream()
                    .map(ChannelResponseDto::channelId)
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
                        if (dto.channelId().equals(channelC.getId())) {
                            assertNotNull(dto.userIds(), "비공개 채널은 참여자 정보 제공");
                            assertTrue(dto.userIds().contains(member1.getId()));
                        } else {
                            assertTrue(dto.userIds().isEmpty(), "공개 채널은 맴버가 없어야한다");
                        }
                    })
            );
        }

    }

    @Nested
    @DisplayName("updateChannel")
    class updateChannel {

        @Test
        @DisplayName("[Integration][Positive] 채널 변경 - Public 채널 수정시 내용 반영된다")
        void updateChannel_updates_public_channel_fields() {
            // given
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );
            Channel channel = channelRepository.save(
                    Channel.createPublicChannel(creator.getId(), "공지", "전체 공지")
            );


            ChannelUpdateRequestDto request = ChannelUpdateRequestDto.builder()
                    .title("수정한 타이틀")
                    .description("수정한 설명란")
                    .build();


            // 변경 전 스냅샷
            UUID channelId = channel.getId();
            Instant beforeUpdatedAt = channel.getUpdatedAt();
            String beforeTitle = channel.getTitle();
            String beforeDesc = channel.getDescription();
            ChannelType beforeType = channel.getType();
            UUID beforeCreatedBy = channel.getCreatedByUserId();
            Instant beforeCreatedAt = channel.getCreatedAt();


            // when
            channelService.updateChannel(channel.getId(), request);

            // then
            Channel updatedChannel = channelRepository.findById(channel.getId()).orElseThrow();
            assertAll(
                    // 식별자/기본 속성 유지
                    () -> assertEquals(channel.getId(), updatedChannel.getId()),
                    () -> assertEquals(channelId, updatedChannel.getId()),
                    () -> assertEquals(beforeType, updatedChannel.getType()),
                    () -> assertEquals(beforeCreatedBy, updatedChannel.getCreatedByUserId()),
                    () -> assertEquals(beforeCreatedAt, updatedChannel.getCreatedAt()),

                    // 변경된 필드
                    () -> assertEquals("수정한 타이틀", updatedChannel.getTitle()),
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
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );
            Channel privateChannel = channelRepository.save(
                    Channel.createPrivateChannel(creator.getId())
            );

            ChannelUpdateRequestDto request = ChannelUpdateRequestDto.builder()
                    .title("수정한 타이틀")
                    .description("수정한 설명란")
                    .build();
            // when & then
            assertThrows(IllegalArgumentException.class, () -> channelService.updateChannel(privateChannel.getId(), request));
        }
    }

    @Nested
    @DisplayName("deleteChannel")
    class deleteChannel {

        @Test
        @DisplayName("[Integration][Positive] 채널 삭제 - 채널/메시지/읽음상태 모두 삭제된다 ")
        void deleteChannel_deletes_channel_and_related_messages_read_statuses() {
            // given
            User creator = userRepository.save(
                    User.builder().nickname("creator").email("c@ex.com")
                            .password("pw").role(RoleType.USER).phoneNumber("010").build()
            );

            User member1 = userRepository.save(
                    User.builder().nickname("member1").email("csdd@ex.com")
                            .password("pssw").role(RoleType.USER).phoneNumber("010-2222-2222").build()
            );

            User member2 = userRepository.save(
                    User.builder().nickname("member2").email("cfffsdd@ex.com")
                            .password("pssssw").role(RoleType.USER).phoneNumber("010-3333-4444").build()
            );
            ChannelCreateRequestDto dto = ChannelCreateRequestDto.builder()
                    .userId(creator.getId())
                    .title("private")
                    .description("private")
                    .type(ChannelType.PRIVATE)
                    .memberIds(List.of(member1.getId(), member2.getId()))
                    .build();

            ChannelCreateCommand cmd = ChannelCreateCommand.from(dto);

            channelService.createChannel(cmd); // TODO: 추후 service 의존도 빼고 repository로 만들것

            // 방금 생긴 채널 식별 (가장 최근/사이즈 차이로 추적)
            List<Channel> afterCreate = channelRepository.findAll();
            Channel channel = afterCreate.get(afterCreate.size() - 1); // in-memory라면 OK (JPA면 정밀 조회 권장)

            // 메시지 2개 저장 + 채널에 messageId 연결 후 채널 재저장
            Message m1 = messageRepository.save(new Message("hello1", channel.getId(), creator.getId(), null));
            Message m2 = messageRepository.save(new Message("hello2", channel.getId(), creator.getId(), null));
            channel.addMessageId(m1.getId());
            channel.addMessageId(m2.getId());
            channelRepository.save(channel); // 삭제 로직이 channel.getMessageIds()를 보므로 반영 필요

            // then
            assertAll(
                    () -> assertTrue(messageRepository.findById(m1.getId()).isPresent()),
                    () -> assertTrue(messageRepository.findById(m2.getId()).isPresent()),
                    () -> assertTrue(!readStatusRepository.findByChannelId(channel.getId()).isEmpty())
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

