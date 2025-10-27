package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.user.UserRequestDto;
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
        // TODO: create - 해당 User id 로 유저 존재 안할시 예외
        @Test
        @DisplayName("[Integration][negative] 유저상태 생성 - 넘겨받은 User id가 조회시 존재 안할시 예외 발생")
        void create_whenUserNotFound_thenThrows() {
            UUID id = UUID.randomUUID();
            assertThrows(NoSuchElementException.class, () -> userStatusService.createUserStatus(id));
        }

        @Test
        @DisplayName("[Integration][negative] 유저상태 생성 - 해당 유저의 중복 상태 등록시 예외 발생")
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
