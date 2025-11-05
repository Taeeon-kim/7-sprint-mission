package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import com.sprint.mission.discodeit.entity.type.RoleType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.store.InMemoryStore;
import org.junit.jupiter.api.*;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceIntegrationTest {

    private UserRepository userRepository;
    private AuthService authService;
    private UserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        authService = new BasicAuthService(userRepository, userStatusRepository);
    }

    @AfterEach
    void tearDown() {

    }

    @Nested
    @DisplayName("checkUser")
    class checkUser {

        @Test
        @DisplayName("[Integration][Flow][Positive] 로그인 - 아이디, 비밀번호 일치 시 회원 정보 반환")
        void login_then_returns_user() {
            User user = User.builder()
                    .nickname("name")
                    .email("email@example.com")
                    .phoneNumber("010-1111-2222")
                    .profileId(null)
                    .role(RoleType.USER)
                    .password("password")
                    .build();

            User saved = userRepository.save(user);

            UserStatus userStatus = new UserStatus(saved.getId());
            userStatusRepository.save(userStatus);

            // when
            UserResponseDto loginUser = authService.login(AuthLoginRequestDto.builder()
                    .username(saved.getNickname())
                    .password(saved.getPassword())
                    .build());

            // then
            assertAll(
                    () -> assertEquals(saved.getId(), loginUser.getId()),
                    () -> assertEquals(saved.getNickname(), loginUser.getNickname()),
                    () -> assertEquals(saved.getProfileId(), loginUser.getProfileId()),
                    () -> assertEquals(saved.getRole(), loginUser.getRole()),
                    () -> assertEquals(saved.getPhoneNumber(), loginUser.getPhoneNumber()),
                    () -> assertEquals(saved.getEmail(), loginUser.getEmail()),
                    () -> assertEquals(UserActiveStatus.ONLINE, loginUser.getIsOnline())
            );
        }


        @Test
        @DisplayName("[Integration][Flow][Negative] 로그인 - 아이디, 비밀번호 불일치 시 NoSuchElement 예외")
        void login_throws_when_not_found() {
            // given
            User user = User.builder()
                    .role(RoleType.USER)
                    .profileId(null)
                    .email("email@example.com")
                    .phoneNumber("010-1111-2222")
                    .nickname("name")
                    .password("password")
                    .build();

            User saved = userRepository.save(user);


            // when & then
            assertAll(
                    () -> assertThrows(NoSuchElementException.class, () -> authService.login(AuthLoginRequestDto.builder()
                            .username(saved.getNickname())
                            .password("wrongPassword")
                            .build())),
                    () -> assertThrows(NoSuchElementException.class, () -> authService.login(AuthLoginRequestDto.builder()
                                    .username("wrongName")
                                    .password(saved.getPassword())
                                    .build()
                            )
                    )
            );
        }
    }

}
