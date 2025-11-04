package com.sprint.mission.discodeit.unit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.type.RoleType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
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
    private UserStatusRepository userStatusRepository;
    private UserStatusService userStatusService;
    private BasicUserService userService;
    private BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userReader = mock(UserReader.class);
        userStatusRepository = mock(UserStatusRepository.class);
        userStatusService = mock(UserStatusService.class);
        binaryContentRepository = mock(BinaryContentRepository.class);
        userService = new BasicUserService(userRepository, userReader, userStatusService, userStatusRepository, binaryContentRepository);
    }

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
            when(userRepository.save(any(User.class)))
                    .thenReturn(User.create(nickname, email, password, RoleType.USER, phone, null));


            // when
            userService.signUp(new UserSignupRequestDto(nickname, email, password, phone, null)); // 흐름검증

            // then
            verify(userRepository, times(1)).save(any(User.class)); // 행위 검증
        }

        @Test
        @DisplayName("[Branch][Negative] 회원가입 - 유효하지않은 입력값일때 IllegalArgumentException 예외 발생 및 repository.save()미호출")
        void signUp_shouldThrowException_whenInValidInput() {
            // isBlank
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("", "a@b.com", "123", "0101111", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", "", "pw", "010", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", "a@b.com", "", "010", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", "a@b.com", "pw", "", null)));

            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto(" ", "a@b.com", "123", "0101111", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", " ", "pw", "010", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", "a@b.com", " ", "010", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", "a@b.com", "pw", " ", null)));

            // null
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto(null, "a@b.com", "123", "0101111", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", null, "pw", "010", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", "a@b.com", null, "010", null)));
            assertThrows(IllegalArgumentException.class, () ->
                    userService.signUp(new UserSignupRequestDto("nick", "a@b.com", "pw", null, null)));

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {
        @Test
        @DisplayName("[Behavior] 회원조회 - userReader.findUserOrThrow(id) 호출 (조회 위임 검증)")
        void getUserById_shouldDelegateToUserReader_whenCalled() {
            // given
            UUID id = UUID.randomUUID();
            when(userStatusRepository
                    .findByUserId(id))
                    .thenReturn(Optional.of(new UserStatus(id)));
            when(userReader.findUserOrThrow(id)).thenReturn(User.create("nickname", "email@exa.com", "pwd", RoleType.USER, "010", null));

            // when
            userService.getUserById(id);

            verify(userReader).findUserOrThrow(id);
        }

        @Test
        @DisplayName("[Behavior + Branch][Positive] 회원조회 - Reader에 위임하고 결과 그대로 반환")
        void getUserById_shouldReturnUser_whenFound() {

            // given
            User user = User.create("taeeon", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(user.getId())).thenReturn(user); // Stub
            when(userStatusRepository.findByUserId(user.getId()))
                    .thenReturn(Optional.of(new UserStatus(user.getId())));

            // when
            UserResponseDto result = userService.getUserById(user.getId()); // 흐름 검증

            assertEquals(user.getNickname(), result.getNickname()); // 분기 검증
            assertEquals(user.getEmail(), result.getEmail());
            assertEquals(user.getPhoneNumber(), result.getPhoneNumber());
            assertEquals(user.getRole(), result.getRole());
            assertEquals(user.getProfileId(), result.getProfileId());

            // then
            verify(userReader).findUserOrThrow(user.getId()); // 행위검증
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
            verify(userReader).findUserOrThrow(null);
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {
        @Test
        @DisplayName("[Behavior] 회원삭제 - userRepository.deleteById() 위임 호출")
        void deleteUser_shouldCallRepositoryDelete_whenValidId() {

            // given
            User user = User.builder()
                    .nickname("taeeon")
                    .role(RoleType.USER)
                    .password("pwd")
                    .email("dfds@exmap.com")
                    .phoneNumber("010-1234-5678")
                    .profileId(UUID.randomUUID())
                    .build();
            UserStatus userStatus = new UserStatus(user.getId());
            when(userReader.findUserOrThrow(any())).thenReturn(user);

            when(userStatusRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(userStatus));
            when(userStatusRepository.deleteById(any())).thenReturn(true);
            when(userRepository.deleteById(user.getId())).thenReturn(true);
//            when(binaryContentRepository);

            // when
            userService.deleteUser(user.getId());

            // then
            verify(userRepository, times(1)).deleteById(user.getId());
            verify(binaryContentRepository).deleteById(any()); // 부수효과, 흐름상 원자적으로 binarycontent도 삭제
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
        @DisplayName("[Exception] 회원수정 - 미존재 유저 일경우 NoSuchElementException 전파")
        void updateUser_shouldThrowException_whenUserNotFound() {
            //given
            UUID id = UUID.randomUUID();
            when(userReader.findUserOrThrow(id)).thenThrow(new NoSuchElementException("not found"));

            //when+then
            assertThrows(
                    NoSuchElementException.class,
                    () -> userService.updateUser(id,
                            new UserUpdateRequestDto(
                                    "new",
                                    null,
                                    null,
                                    null,
                                    null
                            )
                    )
            );

            //then
            verify(userReader).findUserOrThrow(id);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 닉네임 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenNicknameChanged() {

            // given (Stub 설정, 외부 협력자와 반환값 고정)
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);

            // when (행위 실행 : 실제 서비스 호출)
            userService.updateUser(id, new UserUpdateRequestDto("new", null, null, null, null));
            InOrder inOrder = inOrder(userReader, userRepository);

            // then (검증 : 협력자 호출/순서 확인)
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(real);
        }

        @Test
        @DisplayName("[Behavior + Branch][Negative] 회원수정 - 닉네임 미변경시 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenNicknameNotChanged() {

            // given (Stub 설정, 외부 협력자와 반환값 고정)
            UUID id = UUID.randomUUID();
            User real = User.create("same", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);


            // when (행위 실행 : 실제 서비스 호출)
            userService.updateUser(id, new UserUpdateRequestDto("same", null, null, null, null));
            InOrder inOrder = inOrder(userReader, userRepository);

            // then (검증 : 협력자 호출/순서 확인)
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository, never()).save(real);
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 이메일 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenEmailChanged() {
            // given
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);

            // when
            userService.updateUser(id, new UserUpdateRequestDto(null, "change@email.com", null, null, null));

            // then (순서 + 위임 검증)
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 비밀번호 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenPasswordChanged() {
            // given
            UUID id = UUID.randomUUID();

            User real = User.create("nick", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);

            // when
            userService.updateUser(id, new UserUpdateRequestDto(null, null, "vjhsngr", null, null));

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(real);
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 전화번호 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenPhoneChanged() {
            // given
            UUID id = UUID.randomUUID();

            User real = User.create("nick", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);

            // when
            userService.updateUser(id, new UserUpdateRequestDto(null, null, null, "010-9999-9999", null));

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(real);
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 프로필이미지 id 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenProfileIdChanged() {
            // given
            UUID id = UUID.randomUUID();

            User real = User.create("nick", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);
            UUID profileId = UUID.randomUUID();

            // when
            userService.updateUser(id, new UserUpdateRequestDto(null, null, null, null, profileId));

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(real);
        }

        @Test
        @DisplayName("[Behavior + Branch][Negative] 회원수정 - 모든필드 null일때 미변경 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenAllFieldNull() {
            // given
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);

            // when
            userService.updateUser(id, new UserUpdateRequestDto(null, null, null, null, null));

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("[Behavior + Branch][Negative] 회원수정 - 기존과 동일값 일때 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenNoFieldChanged() {
            // given
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", RoleType.USER, "010", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);

            // when
            userService.updateUser(id, new UserUpdateRequestDto("nick", "a@b.com", "pw", "010", null));

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository, never()).save(any());
        }
    }

//    @Nested
//    @DisplayName("getAllUsers")
//    class GetAllUsers {
//        @Test
//        @DisplayName("[Behavior + Flow] 모든회원조회 - 리포지토리 결과를 그대로 반환")
//        void getAllUsers_shouldReturnListFromRepository() {
//            List<User> users = List.of(User.create("a", "a@b.com", "p", RoleType.USER, "010", null));
//            List<UserResponseDto> origin =
//                    users.stream().map(user ->
//
//                    UserResponseDto.from(user, null)
//                    ).toList();
//            when(userRepository.findAll()).thenReturn(users);
//            when(userStatusRepository.findById(any())).thenReturn(users.get(0));
//
//            List<UserResponseDto> result = userService.getAllUsers();
//
//            assertEquals(users, result); // flow 검증 (결과 전달이 잘 되었는가)
//            verify(userRepository).findAll(); // behavior 검증(호출/위임이 잘되었는가)
//        }
//    }

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
            List<User> users = List.of(User.create("a", "a@b.com", "p", RoleType.USER, "010", null));
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