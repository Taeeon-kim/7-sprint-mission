package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.RoleType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

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
        ▣ 테스트 철학 (Intent-Oriented)
          - 서비스 단위 테스트는 “의도 중심(Intent-Oriented)”으로 작성하며,
            그중에서도 “행위(Behavior)”를 중심 축으로 삼는다.
          - 즉, 서비스의 주요 역할이 ‘협력자 간 행위 조율’이므로,
            테스트는 행위가 언제(Branch), 어떤 순서로(Flow),
            어떤 예외 상황에서(Propagation) 발생하는지를 함께 검증한다.

        1) 행위(Behavior)
           - 올바른 협력자를 호출/미호출 하는가? 위임이 일어났는지 확인
             ex) verify(repo).save(user) / verify(repo, never()).save(any()) / ArgumentCaptor, argThat

        2) 흐름/순서(Flow/Sequence)
           - 서비스가 정상 흐름을 따라 동작했는가?
             ex) userService.getUserById(id) -> mock/stub이 설정된 상태에서 서비스 메서드가 정상적으로 실행되고, 중간에 예외나 빠진 단계 없이 “한 사이클이 돈다”는 걸 검증
           - 호출 순서가 의미가 있으면 InOrder로 검증
             ex) inOrder(reader, repo).verify(reader).find... → verify(repo).save(...)

        3) 분기(Branch)
           - 입력/상태에 따라 서로 다른 경로를 타는가?
             * Positive(정상): 조건 충족 시 기대 동작 수행 (예: 변경 O → save 호출)
             * Negative(부정): 조건 불충족 시 동작 차단
               - 입력 가드(예: id == null → IllegalArgumentException, 협력자 미호출)
               - 행위 부정(예: 변경 없음 → save 미호출)
            - 테스트에서는 해당 조건 분기의 결과를 assertion/verify로 검증한다.

        4) 전파(Propagation)
           - 하위 계층(Reader/Repository/Gateway 등)에서 발생한 예외가 서비스에서 적절히 전파되는가?
             ex) when(reader.find...).thenThrow(...) → assertThrows(...)

        ※ 참고사항
         ──────────────────────
        • 위의 테스트 목적들은 서로 독립적이지만, 실제 코드에서는
          하나의 테스트가 두 가지 이상의 목적(행위, 분기 등)을 동시에 검증할 수도 있다.
          같은 검증 코드라도 “테스트 의도”에 따라 해석이 달라질 수 있다.

          ex)
            (1) 단순 위임 확인 → 행위 검증
                verify(repo).save(user);

            (2) 특정 조건 충족 시 호출 → 분기 결과 검증
                verify(repo).save(user);

            (3) 일반적인 서비스 테스트 → 행위 + 분기 검증
                verify(repo).save(user); // 위임이면서 조건 결과

        ※ 엔티티 규칙(값 변경, 불변식 등)은 도메인 테스트에서 검증하고, 서비스 테스트는 협력자 호출/흐름/분기에 집중한다.
    */

    /*
        테스트케이스 명명 규칙
        ──────────────────────
        1) 메서드명: 기능_should결과_when상황  (영문 권장)
           - ex) signUp_shouldCallUserRepositorySave_whenValidInput
           - ex) updateUser_shouldNotCallRepositorySave_whenNoFieldChanged
           - ex) getUserById_shouldPropagateException_whenNotFound

        2) @DisplayName: 의도(Intent) 중심으로 명확하고 가독성 있게 기술
           - ex) [Behavior] 회원가입 - userRepository.save() 호출 (저장 위임 검증)
           - ex) [Branch][Positive] 회원수정 - 필드 변경 시 save 호출 (조건 충족 경로)
           - ex) [Branch][Negative] 회원수정 - 변경 없음이면 save 미호출 (조건 불충족 경로)
           - ex) [Flow] 회원조회 - 정상 입력값일 때 서비스 흐름이 정상적으로 반환됨
           - ex) [Exception] 회원조회 - 미존재 시 NoSuchElementException 전파

        ※ [Positive]/[Negative] 태그는 주로 [Branch] 테스트에서 사용하며,
           [Behavior], [Flow], [Exception]은 일반적으로 단독 사용 가능.
           필요 시 조합 형태([Branch+Behavior])로도 명시 가능.

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
        @DisplayName("[Behavior] 회원가입 - 유효한 입력값일때 userRepository.save() 호출")
        void signUp_shouldCallUserRepositorySave_whenValidInput() {
            // given
            String nickname = "Taeeon";
            String email = "taeeon@test.com";
            String password = "1234";
            String phone = "01012345678";

            // when
            userService.signUp(nickname, email, password, phone); // 흐름검증

            // then
            verify(userRepository, times(1)).save(any(User.class)); // 행위 검증
        }

        @Test
        @DisplayName("[Branch][Negative] 회원가입 - 유효하지않은 입력값일때 IllegalArgumentException 예외 발생 및 repository.save()미호출")
        void signUp_shouldThrowException_whenValidInput() {
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

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {
        @Test
        @DisplayName("[Behavior] 회원조회 - userReader.findUserOrThrow(id) 호출 (조회 위임 검증)")
        void getUserById_shouldDelegateToUserReader_whenCalled() {
            UUID id = UUID.randomUUID();
            userService.getUserById(id);

            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Behavior + Branch][Positive] 회원조회 - Reader에 위임하고 결과 그대로 반환")
        void getUserById_shouldReturnUser_whenFound() {
            UUID id = UUID.randomUUID();
            User user = new User("taeeon", "a@b.com", "pw", RoleType.USER, "010");
            when(userReader.findUserOrThrow(id)).thenReturn(user); // Stub

            User result = userService.getUserById(id); // 흐름 검증
            assertEquals(user, result); // 분기 검증
            verify(userReader).findUserOrThrow(id); // 행위검증
        }

        @Test
        @DisplayName("[Exception] 회원조회 - 기존 회원이 없다면 NoSuchElementException 예외 전파")
        void getUserById_shouldPropagateException_whenReaderThrowNotFound() {
            UUID id = UUID.randomUUID();
            when(userReader.findUserOrThrow(id)).thenThrow(new NoSuchElementException("not found"));

            assertThrows(NoSuchElementException.class, () -> userService.getUserById(id));
        }

        @Test
        @DisplayName("[Branch][Negative] 회원조회 - 유효하지않은 입력값일때 IllegalArgumentException 예외 발생")
        void getUserById_shouldThrowException_whenIdIsInvalid() {
            when(userReader.findUserOrThrow(null)).thenThrow(new IllegalArgumentException("not found"));
            assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null));
            verify(userRepository, never()).findById(any());
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {
        @Test
        @DisplayName("[Behavior] 회원삭제 - userRepository.deleteById() 위임 호출")
        void deleteUser_shouldCallRepositoryDelete_whenValidId() {
            UUID id = UUID.randomUUID();

            userService.deleteUser(id);

            verify(userRepository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("[Branch][Negative] 회원삭제 - 유효하지않은 입력값일때 IllegalArgumentException 발생 및 Repository 미호출")
        void deleteUser_shouldThrowException_whenIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
            verify(userRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {
        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 닉네임 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenNicknameChanged() {

            // given (Stub 설정, 외부 협력자와 반환값 고정)
            UUID id = UUID.randomUUID();
            User mockedUser = mock(User.class);
            when(userReader.findUserOrThrow(id)).thenReturn(mockedUser);
            when(mockedUser.updateNickname("new")).thenReturn(true); // 도메인 변경 발생 가정, 실제 도메인 로직 체크하고싶다면 도메인 테스트에서 할것

            // when (행위 실행 : 실제 서비스 호출)
            userService.updateUser(id, "new", null, null, null);
            InOrder inOrder = inOrder(userReader, userRepository);

            // then (검증 : 협력자 호출/순서 확인)
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(mockedUser);
        }

        @Test
        @DisplayName("[Behavior + Branch][Negative] 회원수정 - 닉네임 미변경시 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenNicknameNotChanged() {

            // given (Stub 설정, 외부 협력자와 반환값 고정)
            UUID id = UUID.randomUUID();
            User mockedUser = mock(User.class);
            when(userReader.findUserOrThrow(id)).thenReturn(mockedUser);
            when(mockedUser.updateNickname("same")).thenReturn(false); // 도메인 변경 발생 가정, 실제 도메인 로직 체크하고싶다면 도메인 테스트에서 할것

            // when (행위 실행 : 실제 서비스 호출)
            userService.updateUser(id, "same", null, null, null);
            InOrder inOrder = inOrder(userReader, userRepository);

            // then (검증 : 협력자 호출/순서 확인)
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository, never()).save(mockedUser);
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 이메일 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenEmailChanged() {
            // given
            UUID id = UUID.randomUUID();
            User mockedUser = mock(User.class);
            when(userReader.findUserOrThrow(id)).thenReturn(mockedUser);
            when(mockedUser.updateEmail("change@email.com")).thenReturn(true);

            // when
            userService.updateUser(id, null, "change@email.com", null, null);

            // then (순서 + 위임 검증)
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(mockedUser);
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 비밀번호 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenPasswordChanged() {
            // given
            UUID id = UUID.randomUUID();
            User mockedUser = mock(User.class);
            when(userReader.findUserOrThrow(id)).thenReturn(mockedUser);
            when(mockedUser.updatePassword("vjhsngr")).thenReturn(true);

            // when
            userService.updateUser(id, null, null, "vjhsngr", null);

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(mockedUser);
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 전화번호 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenPhoneChanged() {
            // given
            UUID id = UUID.randomUUID();
            User mockedUser = mock(User.class);
            when(userReader.findUserOrThrow(id)).thenReturn(mockedUser);
            when(mockedUser.updatePhoneNumber("010-9999-9999")).thenReturn(true);

            // when
            userService.updateUser(id, null, null, null, "010-9999-9999");

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(mockedUser);
        }

        @Test
        @DisplayName("[Behavior + Branch][Negative] 회원수정 - 변경 없음이면 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenNoFieldChanged() {
            // given
            UUID id = UUID.randomUUID();
            User mockedUser = mock(User.class);
            when(userReader.findUserOrThrow(id)).thenReturn(mockedUser);
            // 모든 업데이트가 false (미변경)
            when(mockedUser.updateNickname("name")).thenReturn(false);
            when(mockedUser.updateEmail(null)).thenReturn(false);
            when(mockedUser.updatePassword(null)).thenReturn(false);
            when(mockedUser.updatePhoneNumber(null)).thenReturn(false);

            // when
            userService.updateUser(id, "name", null, null, null);

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getAllUsers")
    class GetAllUsers {
        @Test
        @DisplayName("[Behavior + Flow] 모든회원조회 - 리포지토리 결과를 그대로 반환")
        void getAllUsers_shouldReturnListFromRepository() {
            List<User> users = List.of(new User("a", "a@b.com", "p", RoleType.USER, "010"));
            when(userRepository.findAll()).thenReturn(users);

            List<User> result = userService.getAllUsers();

            assertEquals(users, result); // flow 검증 (결과 전달이 잘 되었는가)
            verify(userRepository).findAll(); // behavior 검증(호출/위임이 잘되었는가)
        }
    }

    @Nested
    @DisplayName("getUsersByIds")
    class GetUsersByIds {
        @Test
        @DisplayName("[Branch][Negative] 특정 회원리스트 조회- 입력이 null이면 IllegalArgumentException 예외 발생")
        void getUsersByIds_shouldThrowException_whenInputNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.getUsersByIds(null));
            verify(userRepository, never()).findAllByIds(any());
        }

        @Test
        @DisplayName("[Behavior + Flow] 특정 회원리스트 조회 - 주어진 id 리스트에 해당하는 유저 목록 반환")
        void getUsersByIds_shouldReturnUsers_whenIdsValid() {
            List<UUID> ids = List.of(UUID.randomUUID());
            List<User> users = List.of(new User("a", "a@b.com", "p", RoleType.USER, "010"));
            when(userRepository.findAllByIds(ids)).thenReturn(users);

            List<User> result = userService.getUsersByIds(ids);

            assertEquals(users, result);
            verify(userRepository).findAllByIds(ids);
        }

        @Test
        @DisplayName("[Behavior + Flow] 특정 회원리스트 조회 - 빈 ID 목록시 빈 리스트 반환 ")
        void getUsersByIds_ShouldReturnEmptyList_whenIdsEmpty() {
            //given
            List<UUID> ids = List.of();
            List<User> users = List.of();
            when(userRepository.findAllByIds(ids)).thenReturn(users);

            //when
            List<User> result = userService.getUsersByIds(ids);

            //then
            assertEquals(users, result);
            verify(userRepository).findAllByIds(ids);
        }
    }
}