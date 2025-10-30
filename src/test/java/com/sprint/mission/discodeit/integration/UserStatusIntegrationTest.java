package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.type.RoleType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import com.sprint.mission.discodeit.store.InMemoryStore;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class UserStatusIntegrationTest {


    // 필드
    private final InMemoryStore store = new InMemoryStore();
    private UserStatusRepository userStatusRepository;
    private UserRepository userRepository;
    private UserReader userReader;
    private UserStatusService userStatusService;
    private UserService userService;
    private BinaryContentRepository binaryContentRepository;

    // 의존성 주입

    // before each
    @BeforeEach
    void setUp() {
        userStatusRepository = new JCFUserStatusRepository(store.userStatusses);
        userRepository = new JCFUserRepository(store.users);
        binaryContentRepository = new JCFBinaryContentRepository(store.binaryContents);
        UserReader userReader = new UserReader(userRepository);
        userStatusService = new BasicUserStatusService(userReader, userStatusRepository);
        userService = new BasicUserService(userRepository, userReader, userStatusService, userStatusRepository, binaryContentRepository);
    }

    @AfterEach
    void teardown() {
        store.userStatusses.clear();
    }

    @Nested
    @DisplayName("createUserStatus")
    class CreateUserStatus {

        @Test
        @DisplayName("[Integration][Flow][Positive] 유저상태 생성 - 생성후 조회시 동일 데이터 반환 ")
        void create_persists_and_returns_same_data() {
            // given
            User user = new User("name", "emaile@example.com", "pwd", RoleType.USER, "010", null);
            User savedUser = userRepository.save(user);
            int before = userStatusRepository.findAll().size();

            // when
            UUID statusId = userStatusService.createUserStatus(new UserStatusRequestDto(savedUser.getId()));
            UserStatus userStatus = userStatusRepository.findById(statusId).orElseThrow(() -> new NoSuchElementException("해당 정보 없음"));

            // then
            int after = userStatusRepository.findAll().size();
            assertAll(
                    () -> assertEquals(before + 1, after),
                    () -> assertEquals(savedUser.getId(), userStatus.getUserId()),
                    () -> assertEquals(UserActiveStatus.ONLINE, userStatus.getUserStatus())
            );
        }

        @Test
        @DisplayName("[Integration][Flow][negative] 유저상태 생성 - 존재하지않는 유저로 등록시 예외 발생")
        void create_whenUserNotFound_thenThrows() {
            UUID id = UUID.randomUUID();
            assertThrows(NoSuchElementException.class, () -> userStatusService.createUserStatus(new UserStatusRequestDto(id)));
        }

        @Test
        @DisplayName("[Integration][Flow][negative] 유저상태 생성 - 이미 등록된 유저 중복 등록시 예외 발생")
        void create_whenDuplicate_thenThrows() {
            // given
            UUID signedUserId = userService.signUp(
                    new UserSignupRequestDto(
                            "name",
                            "email@ee.com",
                            "pwd",
                            "010-1111-2222",
                            null
                    )
            );

            // when & then
            assertThrows(IllegalArgumentException.class,
                    () -> userStatusService.createUserStatus(new UserStatusRequestDto(signedUserId)));

        }

    }

    @Nested
    @DisplayName("getUserStatusById")
    class GetUserStatusById {

        @Test
        @DisplayName("[Integration][Flow][positive] 유저상태 조회 - id 조회시 UserStatusResponseDto로 유저상태정보 반환 성공")
        void getUserStatusById_returns_userStatus() {
            // given
            User user = User.builder()
                    .nickname("name")
                    .email("ab@email.com")
                    .password("password")
                    .phoneNumber("010-1111-2222")
                    .role(RoleType.USER)
                    .profileId(null)
                    .build();

            User savedUser = userRepository.save(user);

            // when
            UUID userStatusId = userStatusService.createUserStatus(new UserStatusRequestDto(savedUser.getId()));
            UserStatusResponseDto userStatusById = userStatusService.getUserStatus(userStatusId);
            // then
            assertAll(
                    () -> assertNotNull(userStatusById),
                    () -> assertEquals(userStatusId, userStatusById.id()),
                    () -> assertEquals(savedUser.getId(), userStatusById.userId()),
                    () -> assertNotNull(userStatusById.lastActiveAt()),
                    () -> assertTrue(userStatusById.lastActiveAt().isBefore(Instant.now().plusSeconds(10)))
            );
        }

        @Test
        @DisplayName("[Integration][Flow][Negative] 유저상태 조회 -존재하지 않는 id 조회시 NoSuchElementException 발생")
        void getUserStatusById_throws_whenNotFound() {
            // given
            UUID id = UUID.randomUUID();

            // when & then
            assertThrows(NoSuchElementException.class, () -> userStatusService.getUserStatus(id));
        }

    }

    @Nested
    @DisplayName("getAllUserStatuses")
    class GetAllUserStatuses {
        @Test
        @DisplayName("[Integration][Flow][Positive] 전체 유저상태 조회 - 데이터 없으면 빈 조회")
        void getAllUserStatuses_returns_emptyList_whenNoUserStatuses() {

            // when
            List<UserStatusResponseDto> allUserStatuses = userStatusService.getAllUserStatuses();

            // then
            assertTrue(allUserStatuses.isEmpty());


        }


        @Test
        @DisplayName("[Integration][Flow][Positive] 전체 유저상태 조회 - 여러 상태가 있으면 모두 반환")
        void getAllUserStatuses_returns_expected_size() {
            // given
            User user = User.builder()
                    .nickname("name")
                    .email("emaile@example.com")
                    .phoneNumber("010-1111-2222")
                    .role(RoleType.USER)
                    .password("pwd")
                    .profileId(null)
                    .build();

            User user2 = User.builder()
                    .nickname("name2")
                    .email("email2@example.com")
                    .phoneNumber("010-2222-4444")
                    .role(RoleType.USER)
                    .password("pwd2")
                    .profileId(null)
                    .build();

            userStatusRepository.save(new UserStatus(user.getId()));
            userStatusRepository.save(new UserStatus(user2.getId()));


            // when
            List<UserStatusResponseDto> allUserStatuses = userStatusService.getAllUserStatuses();
            // then
            assertEquals(2, allUserStatuses.size());

        }
    }

    @Nested
    @DisplayName("updateUserStatusById")
    class UpdateUserStatus {

        @Test
        @DisplayName("[Integration][Flow][Positive] 회원상태 수정 - 기존과 다른값으로 변경 반영")
        void updateUserStatus_then_changedValues() {
            // given
            User user = User.builder()
                    .nickname("name")
                    .email("emaile@example.com")
                    .phoneNumber("010-1111-2222")
                    .role(RoleType.USER)
                    .password("pwd")
                    .profileId(null)
                    .build();

            UserStatus savedStatus = userStatusRepository.save(new UserStatus(user.getId()));
            Instant before = savedStatus.getLastActiveAt(); // 스냅샷
            UserStatusUpdateRequestDto updateDto = new UserStatusUpdateRequestDto(Instant.now());

            // when
            userStatusService.updateUserStatus(savedStatus.getId(), updateDto);

            // then
            UserStatus userStatus = userStatusRepository.findById(savedStatus.getId()).orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            assertNotEquals(userStatus.getLastActiveAt(), before);
            assertEquals(userStatus.getLastActiveAt(), updateDto.lastActiveAt());
        }

        @Test
        @DisplayName("[Integration][Flow][Negative] 회원상태 수정 - 존재하지않는 id로 수정시 NoSuchElementException 예외")
        void updateUserStatus_throws_whenIdNotFound() {
            UUID id = UUID.randomUUID();
            UserStatusUpdateRequestDto updateDto = new UserStatusUpdateRequestDto(Instant.now());
            assertThrows(NoSuchElementException.class,
                    () -> userStatusService.updateUserStatus(id, updateDto));
        }

        @Test
        @DisplayName("[Integration][Flow][Negative] 회원상태 수정 - 동일 값이면 변화없음 ")
        void updateUserStatus_noop_whenSameValue() {
            // given
            User user = User.builder()
                    .nickname("name")
                    .email("emaile@example.com")
                    .phoneNumber("010-1111-2222")
                    .role(RoleType.USER)
                    .password("pwd")
                    .profileId(null)
                    .build();

            UserStatus savedStatus = userStatusRepository.save(new UserStatus(user.getId()));

            UserStatusUpdateRequestDto dto = new UserStatusUpdateRequestDto(savedStatus.getLastActiveAt());

            // when
            userStatusService.updateUserStatus(savedStatus.getId(), dto);

            // then
            UserStatus after = userStatusRepository.findById(savedStatus.getId()).orElseThrow();
            // 예: 동일값이면 변화 없음(정책에 맞게 선택)
            assertEquals(savedStatus.getLastActiveAt(), after.getLastActiveAt());
        }


    }

    @Nested
    @DisplayName("UpdateUserStatusByUserId")
    class UpdateUserStatusByUserId {

        @Test
        @DisplayName("[Integration][Flow][Positive] 회원상태 수정 - 기존과 다른값으로 변경 반영")
        void updateUserStatusByUserId_then_changedValues() {
            // given
            User user = User.builder()
                    .nickname("name")
                    .email("emaile@example.com")
                    .phoneNumber("010-1111-2222")
                    .role(RoleType.USER)
                    .password("pwd")
                    .profileId(null)
                    .build();

            UserStatus savedStatus = userStatusRepository.save(new UserStatus(user.getId()));
            Instant before = savedStatus.getLastActiveAt(); // 스냅샷
            UserStatusUpdateRequestDto updateDto = new UserStatusUpdateRequestDto(Instant.now());

            // when
            userStatusService.updateUserStatusByUserId(savedStatus.getUserId(), updateDto);

            // then
            UserStatus userStatus = userStatusRepository.findByUserId(savedStatus.getUserId()).orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            assertNotEquals(userStatus.getLastActiveAt(), before);
            assertEquals(userStatus.getLastActiveAt(), updateDto.lastActiveAt());
        }

        @Test
        @DisplayName("[Integration][Flow][Negative] 회원상태 수정 - 존재하지않는 id로 수정시 NoSuchElementException 예외")
        void updateUserStatusByUserId_throws_whenIdNotFound() {
            UUID id = UUID.randomUUID();
            UserStatusUpdateRequestDto updateDto = new UserStatusUpdateRequestDto(Instant.now());
            assertThrows(NoSuchElementException.class,
                    () -> userStatusService.updateUserStatusByUserId(id, updateDto));
        }

        @Test
        @DisplayName("[Integration][Flow][Negative] 회원상태 수정 - 동일 값이면 변화없음 ")
        void updateUserStatusByUserId_noop_whenSameValue() {
            // given
            User user = User.builder()
                    .nickname("name")
                    .email("emaile@example.com")
                    .phoneNumber("010-1111-2222")
                    .role(RoleType.USER)
                    .password("pwd")
                    .profileId(null)
                    .build();

            UserStatus savedStatus = userStatusRepository.save(new UserStatus(user.getId()));

            UserStatusUpdateRequestDto dto = new UserStatusUpdateRequestDto(savedStatus.getLastActiveAt());

            // when
            userStatusService.updateUserStatusByUserId(savedStatus.getUserId(), dto);

            // then
            UserStatus after = userStatusRepository.findByUserId(savedStatus.getUserId()).orElseThrow();
            // 예: 동일값이면 변화 없음(정책에 맞게 선택)
            assertEquals(savedStatus.getLastActiveAt(), after.getLastActiveAt());
        }
    }

    @Nested
    @DisplayName("DeleteUserStatus")
    class DeleteUserStatus {

        @Test
        @DisplayName("[Integration][Flow][Positive] 회원상태 삭제 - 삭제 후 조회 불가 & 개수 감소")
        void deleteUserStatus_then_not_found_and_size_decreased() {
            // given
            User user = User.builder()
                    .nickname("name")
                    .email("emaile@example.com")
                    .phoneNumber("010-1111-2222")
                    .role(RoleType.USER)
                    .password("pwd")
                    .profileId(null)
                    .build();

            UserStatus savedStatus = userStatusRepository.save(new UserStatus(user.getId()));
            long before = userStatusRepository.findAll().size();

            //when
            userStatusService.deleteUserStatus(savedStatus.getId());

            // then
            long after = userStatusRepository.findAll().size();
            assertAll(
                    () -> assertEquals(before - 1, after),
                    () -> assertThrows(
                            NoSuchElementException.class,
                            () -> userStatusRepository.findById(savedStatus.getId()).orElseThrow()
                    )
            );
        }
    }


}
