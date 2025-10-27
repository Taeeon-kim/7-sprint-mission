package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.user.UserRequestDto;
import com.sprint.mission.discodeit.entity.RoleType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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

    // 의존성 주입

    // before each
    @BeforeEach
    void setUp() {
        userStatusRepository = new JCFUserStatusRepository(store.userStatusses);
        userRepository = new JCFUserRepository(store.users);
        UserReader userReader = new UserReader(userRepository);
        userStatusService = new BasicUserStatusService(userReader, userStatusRepository);
        userService = new BasicUserService(userRepository, userReader, userStatusService, userStatusRepository);
    }

    @AfterEach
    void teardown() {
        store.userStatusses.clear();
    }

    @Nested
    @DisplayName("create")
    class Create {


        @Test
        @DisplayName("[Integration][Flow][Positive] 유저상태 생성 - 생성후 조회시 동일 데이터 반환 ")
        void create_persists_and_returns_same_data() {
            // given
            User user = new User("name", "emaile@example.com", "pwd", RoleType.USER, "010", null);
            User savedUser = userRepository.save(user);
            int before = userStatusRepository.findAll().size();

            // when
            UUID statusId = userStatusService.createUserStatus(savedUser.getId());
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
            assertThrows(NoSuchElementException.class, () -> userStatusService.createUserStatus(id));
        }

        @Test
        @DisplayName("[Integration][Flow][negative] 유저상태 생성 - 이미 등록된 유저 중복 등록시 예외 발생")
        void create_whenDuplicate_thenThrows() {
            // given
            UUID signedUserId = userService.signUp(
                    new UserRequestDto(
                            "name",
                            "email@ee.com",
                            "pwd",
                            "010-1111-2222",
                            null
                    )
            );

            // when & then
            assertThrows(IllegalArgumentException.class,
                    () -> userStatusService.createUserStatus(signedUserId));

        }

    }


}
