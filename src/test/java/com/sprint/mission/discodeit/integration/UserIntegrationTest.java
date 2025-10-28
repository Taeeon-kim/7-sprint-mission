package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class UserIntegrationTest {
    private InMemoryStore store = new InMemoryStore(); // JCF용 인메모리, fake
    private UserService userService;
    private UserRepository userRepository;
    private UserReader userReader;
    private UserStatusRepository userStatusRepository;
    private UserStatusService userStatusService;

    // TODO: SpringBoot, Autowire 로 변경,
    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository(store.users);
        userReader = new UserReader(userRepository);
        userStatusRepository = new JCFUserStatusRepository(store.userStatusses);
        userStatusService = new BasicUserStatusService(userReader, userStatusRepository);
        userService = new BasicUserService(userRepository, userReader, userStatusService, userStatusRepository);
    }

    @AfterEach
    void tearDown() {
        store.users.clear(); // 인메모리 초기화
        store.userStatusses.clear();
    }

    @Nested
    @DisplayName("signUp")
    class SignUp {
        @Test
        @DisplayName("[Integration][Flow][Positive] 회원가입 - 저장 후 조회 시 동일 데이터 반환")
        void signUp_then_persists_and_returns_same_data() {
            // given
            int before = userRepository.findAll().size();

            //when
            UUID id = userService.signUp(new UserSignupRequestDto("name", "example@email.com", "password", "010-1111-2222", null));

            //then
            int after = userRepository.findAll().size();
            assertEquals(before + 1, after);
            User persistedUser = userRepository.findById(id)
                    .orElseThrow(() -> new AssertionError("User not found"));
            assertEquals("name", persistedUser.getNickname());

            assertEquals("example@email.com", persistedUser.getEmail());
            assertEquals("password", persistedUser.getPassword());
            assertEquals("010-1111-2222", persistedUser.getPhoneNumber());

        }

        @Test
        @DisplayName("[Integration][Negative] 회원가입 - 잘못된 입력은 예외 & DB 변화 없음")
        void signUp_invalid_blocked() {
            assertThrows(IllegalArgumentException.class,
                    () -> userService.signUp(new UserSignupRequestDto("", "a@b.com", "pw", "010", null)));
            assertEquals(0, userRepository.findAll().size());
        }

        @Test
        @DisplayName("[Integration][Negative] 회원가입 - 중복된 이메일일 경우 예외")
        void signup_whenDuplicate_email_thenThrows() {
            // given
            userService.signUp(new UserSignupRequestDto("name", "example@email.com", "password", "010-1111-2222", null));
            int before = userRepository.findAll().size();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> userService.signUp(new UserSignupRequestDto("different", "example@email.com", "password", "010-1111-2222", null)));

            // then
            int after = userRepository.findAll().size();
            assertEquals(before, after);
        }

        @Test
        @DisplayName("[Integration][Negative] 회원가입 - 중복된 이름일 경우 예외")
        void signup_whenDuplicate_nickname_thenThrows() {
            // given
            userService.signUp(new UserSignupRequestDto("name", "example@email.com", "password", "010-1111-2222", null));
            int before = userRepository.findAll().size();

            // when & then
            assertThrows(IllegalArgumentException.class, () -> userService.signUp(new UserSignupRequestDto("name", "different@email.com", "password", "010-1111-2222", null)));

            // then
            int after = userRepository.findAll().size();
            assertEquals(before, after);
        }

        @Test
        @DisplayName("[Integraton][Flow][Positive] 회원가입 - UserStatus도 함께 생성된다.")
        void signup_then_userStatus_persists_as_well() {

            // given
            UUID uuid = userService.signUp(
                    new UserSignupRequestDto("name",
                            "example@email.com",
                            "password",
                            "010-1111-2222",
                            null)
            );

            // when
            UserStatus userStatusbByUserId = userStatusRepository
                    .findByUserId(uuid).orElseThrow(() -> new NoSuchElementException("회원정보없음"));

            // then
            assertNotNull(userStatusbByUserId);
            assertEquals(uuid, userStatusbByUserId.getUserId());

        }

    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {

        @Test
        @DisplayName("[Integration][Flow][Positive] 회원조회 - 저장후 response DTO 반환 조회 성공")
        void getUserById_returns_saved_user() {
            //given
            UUID id = userService.signUp(new UserSignupRequestDto("name", "example@email.com", "password", "010-1111-2222", null));

            //when
            UserResponseDto userById = userService.getUserById(id);

            //then
            assertEquals("name", userById.getNickname());
            assertEquals(id, userById.getUserStatus().getUserId());
        }

        @Test
        @DisplayName("[Integration][Exception] 회원조회 - 미존재 → 예외 전파")
        void getUserById_throws_when_not_found() {
            assertThrows(NoSuchElementException.class,
                    () -> userService.getUserById(UUID.randomUUID()));
        }

        @Test
        @DisplayName("[Integration][Flow][Positive] 회원조회 - 회원 상태 정보도 가져온다.")
        void getUserById_returns_user_status() {

        }


    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {
        @Test
        @DisplayName("[Integration][Flow][Positive] 회원수정 - 이메일만 변경, 나머지 유지")
        void updateUser_updates_only_email() throws InterruptedException {
            //given
            UUID id = userService.signUp(new UserSignupRequestDto("nick", "a@b.com", "pw", "010", null));
            Instant beforeTime = userRepository.findById(id).orElseThrow().getUpdatedAt();
            //when
            userService.updateUser(new UserUpdateRequestDto(id, null, "b@c.com", null, null, null));
            //then
            User after = userRepository.findById(id).orElseThrow();
            assertEquals("b@c.com", after.getEmail());
            assertEquals("nick", after.getNickname());
            assertEquals("010", after.getPhoneNumber());

            // 실제 update호출안된건지 update값 조회
            assertTrue(after.getUpdatedAt().isAfter(beforeTime));
        }

        @Test
        @DisplayName("[Integration][Negative] 회원수정 - 변경 없음 → 상태 변화 없음")
        void updateUser_no_effect_when_same_values() {

            //given
            UUID id = userService.signUp(new UserSignupRequestDto("nick", "a@b.com", "pw", "010", null));
            User before = userRepository.findById(id).orElseThrow();
            Instant beforeTime = before.getUpdatedAt(); // 스냅샷

            //when
            userService.updateUser(new UserUpdateRequestDto(id, "nick", "a@b.com", "pw", "010", null));
            userService.updateUser(new UserUpdateRequestDto(id, null, null, null, null, null));

            //then
            User after = userRepository.findById(id).orElseThrow();
            assertEquals(before.getNickname(), after.getNickname());
            assertEquals(before.getEmail(), after.getEmail());
            assertEquals(before.getPhoneNumber(), after.getPhoneNumber());

            assertEquals(beforeTime, after.getUpdatedAt());
        }

        @Test
        @DisplayName("[Integration][State] 회원수정 - 여러 필드 동시 변경 반영")
        void updateUser_updates_multiple_fields() {
            // given
            UUID id = userService.signUp(new UserSignupRequestDto("nick", "a@b.com", "pw", "010", null));
            Instant beforeTime = userRepository.findById(id).orElseThrow().getUpdatedAt();
            UUID profileId = UUID.randomUUID();
            // when
            userService.updateUser(new UserUpdateRequestDto(id, "nick2", "b@c.com", "pw2", "011", profileId));
            User after = userRepository.findById(id).orElseThrow();
            assertEquals("nick2", after.getNickname());
            assertEquals("b@c.com", after.getEmail());
            assertEquals("011", after.getPhoneNumber());
            assertEquals(profileId, after.getProfileId());

            assertTrue(after.getUpdatedAt().isAfter(beforeTime));


        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {
        @Test
        @DisplayName("[Integration][Flow] 회원삭제 - 삭제 후 조회 시 예외")
        void deleteUser_delete_then_not_found() {
            UUID id = userService.signUp(new UserSignupRequestDto("nick", "a@b.com", "pw", "010", null));
            userService.deleteUser(id);
            assertThrows(NoSuchElementException.class, () -> userService.getUserById(id));
        }
    }

    @Nested
    @DisplayName("getAllUsers")
    class GetAllUsers {
        @Test
        @DisplayName("[Integration][Flow] 회원전체조회 - 여러 명 저장 후 전체 조회")
        void getAllUsers_returns_all() {
            userService.signUp(new UserSignupRequestDto("a", "a@a.com", "p", "010", null));
            userService.signUp(new UserSignupRequestDto("b", "b@b.com", "p", "010", null));
            assertEquals(2, userService.getAllUsers().size());
        }
    }

    @Nested
    @DisplayName("getUsersByIds")
    class GetUsersByIds {
        @Test
        @DisplayName("[Integration][Flow] 특정 회원리스트 조회 - 일부 id만 유효 → 유효한 것만 반환")
        void getUsersByIds_returns_only_existing() {
            UUID id1 = userService.signUp(new UserSignupRequestDto("a", "a@a.com", "p", "010", null));
            UUID id2 = UUID.randomUUID();
            List<User> result = userService.getUsersByIds(List.of(id1, id2));
            assertEquals(1, result.size());
            assertEquals(id1, result.get(0).getId());
        }

        @Test
        @DisplayName("[Integration][Boundary] 빈 리스트 → 빈 반환")
        void getUsersByIds_empty_ids_returns_empty() {
            assertTrue(userService.getUsersByIds(List.of()).isEmpty());
        }

        @Test
        @DisplayName("[Integration][Negative] null 입력 → 예외")
        void getUsersByIds_null_ids_throws() {
            assertThrows(IllegalArgumentException.class, () -> userService.getUsersByIds(null));
        }
    }

}


