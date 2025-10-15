package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.RoleType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasicUserServiceTest {

    private UserRepository userRepository;
    private UserReader userReader;
    private BasicUserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userReader = mock(UserReader.class);
        userService = new BasicUserService(userRepository, userReader);
    }
    /*
        Service 테스트 범위(목적)
        ──────────────────────
        1) 행위(Behavior)
           - 올바른 협력자를 호출/미호출 하는가?
             ex) verify(repo).save(user) / verify(repo, never()).save(any()) / ArgumentCaptor, argThat

        2) 흐름/순서(Flow/Sequence)
           - 호출 순서가 의미가 있으면 InOrder로 검증
             ex) inOrder(reader, repo).verify(reader).find... → verify(repo).save(...)

        3) 분기(Branch)
           - 입력/상태에 따라 서로 다른 경로를 타는가?
             * Positive(정상): 조건 충족 시 기대 동작 수행 (예: 변경 O → save 호출)
             * Negative(부정): 조건 불충족 시 동작 차단
               - 입력 가드(예: id == null → IllegalArgumentException, 협력자 미호출)
               - 행위 부정(예: 변경 없음 → save 미호출)

        4) 전파(Propagation)
           - 하위 계층(Reader/Repository/Gateway 등)에서 발생한 예외가 서비스에서 적절히 전파되는가?
             ex) when(reader.find...).thenThrow(...) → assertThrows(...)

        ※ 엔티티 규칙(값 변경, 불변식 등)은 도메인 테스트에서 검증하고, 서비스 테스트는 협력자 호출/흐름/분기에 집중한다.
    */

    /*
        테스트케이스 명명 규칙
        ──────────────────────
        1) 메서드명: 기능_should결과_when상황  (영문 권장)
           - ex) signUp_shouldCallUserRepositorySave_whenValidInput
           - ex) updateUser_shouldNotCallRepositorySave_whenNoFieldChanged
           - ex) getUserById_shouldPropagateException_whenNotFound

        2) @DisplayName: 도메인 톤으로 가독성 있게 기술
           - ex) [Positive] 회원수정 - 필드 변경 시 save 호출
           - ex) [Negative] 회원수정 - 변경 없음이면 save 미호출
           - ex) [Exception] 회원조회 - 미존재 시 NoSuchElementException 전파

        3) 도메인 중심 vs 구현 중심
           - 서비스 테스트는 보통 "구현/행위 중심"으로 네이밍(협력자 호출/전파/분기 확인)
             ex) signUp_shouldCallUserRepositorySave_whenValidInput (O)
           - "도메인 결과 중심" 표현(예: ~shouldSaveNewUser)은 엔티티/통합 테스트에서 사용 권장 (서비스 단위에선 혼동 유발)

        4) 한 테스트 = 한 사실(주장)만 검증(AAA/GWT 분리)
           - Given(픽스처/Stub) → When(한 줄) → Then(단언/verify)
    */

    // --- grouped by use-case with @Nested ---

    @Nested
    @DisplayName("signUp")
    class SignUp {
        @Test
        @DisplayName("[Positive] 회원가입 - 유효한 입력값일때 userRepository.save() 호출")
        void signUp_shouldCallUserRepositorySave_whenValidInput() {
            // given
            String nickname = "Taeeon";
            String email = "taeeon@test.com";
            String password = "1234";
            String phone = "01012345678";

            // when
            userService.signUp(nickname, email, password, phone);

            // then
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("[Negative] 회원가입 - 유효하지않은 입력값일때 IllegalArgumentException 예외 발생")
        void signUp_shouldThrowException_whenInputIsInvalid() {
            // isBlank
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("", "a@b.com", "123", "0101111"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", "", "pw", "010"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", "a@b.com", "", "010"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", "a@b.com", "pw", ""));

            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(" ", "a@b.com", "123", "0101111"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", " ", "pw", "010"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", "a@b.com", " ", "010"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", "a@b.com", "pw", " "));

            // null
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(null, "a@b.com", "123", "0101111"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", null, "pw", "010"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", "a@b.com", null, "010"));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp("nick", "a@b.com", "pw", null));
        }
    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {
        @Test
        @DisplayName("[Positive] 회원조회 - 기존회원이 있다면 userReader.findUserOrThrow(id) 호출")
        void getUserById_shouldCallUserReaderFindUserOrThrow_whenFound() {
            UUID id = UUID.randomUUID();
            userService.getUserById(id);

            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Positive] 회원조회 - 기존회원이 있다면 해당 회원 반환")
        void getUserById_shouldReturnUser_whenFound() {
            UUID id = UUID.randomUUID();
            User user = new User("taeeon", "a@b.com", "pw", RoleType.USER, "010");
            when(userReader.findUserOrThrow(id)).thenReturn(user); // 행위검증

            User result = userService.getUserById(id); // 흐름 검증

            assertEquals(user, result); // 분기 검증
            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Exception] 회원조회 - 기존 회원이 없다면 NoSuchElementException 예외 전파")
        void getUserById_shouldPropagateException_whenReaderThrowNotFound() {
            UUID id = UUID.randomUUID();
            when(userReader.findUserOrThrow(id)).thenThrow(new NoSuchElementException("not found"));

            assertThrows(NoSuchElementException.class, () -> userService.getUserById(id));
        }

        @Test
        @DisplayName("[Negative] 회원조회 - 유효하지않은 입력값일때 IllegalArgumentException 예외 발생")
        void getUserById_shouldThrowException_whenIdIsInvalid() {
            when(userReader.findUserOrThrow(null)).thenThrow(new IllegalArgumentException("not found"));
            assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null));
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {
        @Test
        @DisplayName("[Positive] 회원삭제 - 유효한 ID이면 userRepository.deleteById() 호출")
        void deleteUser_shouldCallRepositoryDelete_whenValidId() {
            UUID id = UUID.randomUUID();

            userService.deleteUser(id);

            verify(userRepository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("[Negative] 회원삭제 - 유효하지않은 null 입력값일때 IllegalArgumentException 예외 발생")
        void deleteUser_shouldThrowException_whenIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
        }
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {
        @Test
        @DisplayName("[Positive] 회원수정 - 닉네임 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenNicknameChanged() {
            UUID id = UUID.randomUUID();
            User user = new User("name", "aaa@emaile.com", "pw", RoleType.USER, "010-1111-1111");
            when(userReader.findUserOrThrow(id)).thenReturn(user);

            userService.updateUser(id, "newName", null, null, null);

            verify(userRepository).save(user);
            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Positive] 회원수정 - 이메일 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenEmailChanged() {
            UUID id = UUID.randomUUID();
            User user = new User("name", "aaa@emaile.com", "pw", RoleType.USER, "010-1111-1111");
            when(userReader.findUserOrThrow(id)).thenReturn(user);

            userService.updateUser(id, null, "change@email.com", null, null);

            verify(userRepository).save(user);
            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Positive] 회원수정 - 비밀번호 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenPasswordChanged() {
            UUID id = UUID.randomUUID();
            User user = new User("name", "aaa@emaile.com", "pw", RoleType.USER, "010-1111-1111");
            when(userReader.findUserOrThrow(id)).thenReturn(user);

            userService.updateUser(id, null, null, "vjhsngr", null);

            verify(userRepository).save(user);
            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Positive] 회원수정 - 전화번호 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenPhoneChanged() {
            UUID id = UUID.randomUUID();
            User user = new User("name", "aaa@emaile.com", "pw", RoleType.USER, "010-1111-1111");
            when(userReader.findUserOrThrow(id)).thenReturn(user);

            userService.updateUser(id, null, null, null, "010-9999-9999");

            verify(userRepository).save(user);
            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Negative] 회원수정 - 변경 없음이면 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenNoFieldChanged() {
            UUID id = UUID.randomUUID();
            User user = new User("name", "aaa@emaile.com", "pw", RoleType.USER, "010-1111-1111");
            when(userReader.findUserOrThrow(id)).thenReturn(user);

            userService.updateUser(id, "name", null, null, null);

            verify(userRepository, never()).save(user);
            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Negative] 회원수정 - ID가 null이면 IllegalArgumentException 및 협력자 미호출")
        void updateUser_shouldThrow_whenIdNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> userService.updateUser(null, "n", null, null, null));
            verify(userReader, never()).findUserOrThrow(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("[Exception] 회원수정 - 찾은 회원이 없을때 NoSuchElementException 예외 전파 및 save 미호출")
        void updateUser_shouldPropagateException_whenUserNotFound() {
            UUID id = UUID.randomUUID();
            when(userReader.findUserOrThrow(id)).thenThrow(new NoSuchElementException("not found"));

            assertThrows(NoSuchElementException.class, () -> userService.updateUser(id, "a", "b", "c", "d"));
            verify(userReader).findUserOrThrow(id);
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getAllUsers")
    class GetAllUsers {
        @Test
        @DisplayName("리포지토리에서 반환된 리스트를 그대로 반환")
        void getAllUsers_shouldReturnListFromRepository() {
            List<User> users = List.of(new User("a", "a@b.com", "p", RoleType.USER, "010"));
            when(userRepository.findAll()).thenReturn(users);

            List<User> result = userService.getAllUsers();

            assertEquals(1, result.size());
            verify(userRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getUsersByIds")
    class GetUsersByIds {
        @Test
        @DisplayName("입력이 null이면 IllegalArgumentException 예외 발생")
        void getUsersByIds_shouldThrowException_whenInputNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.getUsersByIds(null));
        }

        @Test
        @DisplayName("유효한 ID 리스트면 해당 유저 목록 반환")
        void getUsersByIds_shouldReturnUsers_whenIdsValid() {
            List<UUID> ids = List.of(UUID.randomUUID());
            List<User> users = List.of(new User("a", "a@b.com", "p", RoleType.USER, "010"));
            when(userRepository.findAllByIds(ids)).thenReturn(users);

            List<User> result = userService.getUsersByIds(ids);

            assertEquals(1, result.size());
            verify(userRepository).findAllByIds(ids);
        }
    }
}