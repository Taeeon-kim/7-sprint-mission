package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.RoleType;
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
            UserStatusResponseDto userStatusById = userStatusService.getUserStatusById(userStatusId);
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
            assertThrows(NoSuchElementException.class, () -> userStatusService.getUserStatusById(id));
        }

    }


}
