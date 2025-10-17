package com.sprint.mission.discodeit.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User newUser() {
        return new User("name", "example@email.com", "password123", RoleType.USER, "010-1111-1111");
    }

    @Nested
    @DisplayName(" User 생성")
    class ConstructorInvariant {

        @Test
        @DisplayName("[Invariant] 필수입력값 유효하지않으면 예외")
        void constructor_shouldThrowException_whenRequiredFieldsInvalid() {
            assertThrows(IllegalArgumentException.class,
                    () -> new User(null, null, null, null, null));
            assertThrows(IllegalArgumentException.class,
                    () -> new User(null, "a@b.com", "pw", RoleType.USER, "010"));
            assertThrows(IllegalArgumentException.class,
                    () -> new User("nick", "invalid", "pw", RoleType.USER, "010"));
            assertThrows(IllegalArgumentException.class,
                    () -> new User("nick", "a@b.com", "", RoleType.USER, "010"));
            assertThrows(IllegalArgumentException.class,
                    () -> new User("nick", "a@b.com", "pw", null, "010"));
        }


        @Test
        @DisplayName("[Invariant][Negative] 이메일 형식이 잘못되면 예외")
        void constructor_shouldThrow_whenEmailInvalid() {
            assertThrows(IllegalArgumentException.class,
                    () -> new User("name", "invalid", "pw", RoleType.USER, "010"));
        }

        @Test
        @DisplayName("[Invariant][Positive] 유효한 인자면 정상 생성")
        void constructor_shouldCreate_whenValid() {
            User u = newUser();
            assertEquals("name", u.getNickname());
            assertEquals("a@b.com", u.getEmail());
            assertEquals("pw", u.getPassword());
            assertEquals(RoleType.USER, u.getRole());
            assertEquals("010", u.getPhoneNumber());
            assertNotNull(u.getId());
            assertNotNull(u.getCreatedAt());
            assertNotNull(u.getUpdatedAt());
        }
    }


    @Nested
    @DisplayName("User 닉네임 변경 규칙")
    class NicknameRule {
        @Test
        @DisplayName("[Rule][Positive] 유효한 닉네임이면 변경된다")
        void updateNickname_shouldChange_whenValid() {
            User u = newUser();
            boolean changed = u.updateNickname("nick2");

            assertTrue(changed);
            assertEquals("nick2", u.getNickname());
        }

        @Test
        @DisplayName("[Rule][Negative] 닉네임이 공백이면 변경되지 않는다")
        void updateNickname_shouldNotChange_whenBlank() {
            User u = newUser();
            assertFalse(u.updateNickname(""));
            assertEquals("name", u.getNickname());
        }

        @Test
        @DisplayName("[Rule][Negative] 닉네임이 null이면 변경되지 않는다")
        void updateNickname_shouldNotChange_whenNull() {
            User u = newUser();
            assertFalse(u.updateNickname(null));
            assertEquals("name", u.getNickname());
        }

        @Test
        @DisplayName("[Rule][Negative] 닉네임이 동일하면 변경되지 않는다")
        void updateNickname_shouldNotChange_whenSame() {
            User u = newUser();
            assertFalse(u.updateNickname("name"));
            assertEquals("name", u.getNickname());
        }
    }

    @Nested
    @DisplayName("User 이메일 변경 규칙")
    class EmailRule {
        @Test
        @DisplayName("[Rule][Positive] 유효하고 기존과 다르면 변경된다")
        void updateEmail_shouldChange_whenValidAndDifferent() {
            User u = newUser();
            assertTrue(u.updateEmail("b@c.com"));
            assertEquals("b@c.com", u.getEmail());
        }

        @Test
        @DisplayName("[Rule][Negative] 이메일이 null/blank면 변경되지 않는다 (형식 검사는 생성 시에만)")
        void updateEmail_shouldNotChange_whenNullOrBlank() {
            User u = newUser();
            assertFalse(u.updateEmail(null));
            assertEquals("a@b.com", u.getEmail());

            assertFalse(u.updateEmail(""));
            assertEquals("a@b.com", u.getEmail());
        }

        @Test
        @DisplayName("[Rule][Negative] 이메일이 동일하면 변경되지 않는다")
        void updateEmail_shouldNotChange_whenSame() {
            User u = newUser();
            assertFalse(u.updateEmail("a@b.com"));
            assertEquals("a@b.com", u.getEmail());
        }
    }

    @Nested
    @DisplayName("User 비밀번호 변경 규칙")
    class PasswordRule {
        @Test
        @DisplayName("[Rule][Positive] 유효하고 기존과 다르면 변경된다")
        void updatePassword_shouldChange_whenValidAndDifferent() {
            User u = newUser();
            assertTrue(u.updatePassword("pw2"));
            assertEquals("pw2", u.getPassword());
        }

        @Test
        @DisplayName("[Rule][Negative] 비밀번호가 null/blank면 변경되지 않는다")
        void updatePassword_shouldNotChange_whenNullOrBlank() {
            User u = newUser();
            assertFalse(u.updatePassword(null));
            assertEquals("pw", u.getPassword());

            assertFalse(u.updatePassword(""));
            assertEquals("pw", u.getPassword());
        }

        @Test
        @DisplayName("[Rule][Negative] 비밀번호가 동일하면 변경되지 않는다")
        void updatePassword_shouldNotChange_whenSame() {
            User u = newUser();
            assertFalse(u.updatePassword("pw"));
            assertEquals("pw", u.getPassword());
        }
    }

    @Nested
    @DisplayName("User 전화번호 변경 규칙")
    class PhoneRule {
        @Test
        @DisplayName("[Rule][Positive] 유효하고 기존과 다르면 변경된다")
        void updatePhone_shouldChange_whenValidAndDifferent() {
            User u = newUser();
            assertTrue(u.updatePhoneNumber("011"));
            assertEquals("011", u.getPhoneNumber());
        }

        @Test
        @DisplayName("[Rule][Negative] 전화번호가 null/blank면 변경되지 않는다")
        void updatePhone_shouldNotChange_whenNullOrBlank() {
            User u = newUser();
            assertFalse(u.updatePhoneNumber(null));
            assertEquals("010", u.getPhoneNumber());

            assertFalse(u.updatePhoneNumber(""));
            assertEquals("010", u.getPhoneNumber());
        }

        @Test
        @DisplayName("[Rule][Negative] 전화번호가 동일하면 변경되지 않는다")
        void updatePhone_shouldNotChange_whenSame() {
            User u = newUser();
            assertFalse(u.updatePhoneNumber("010"));
            assertEquals("010", u.getPhoneNumber());
        }
    }

    @Nested
    @DisplayName("User 복사 생성자 상태")
    class CopyConstructorState {

        @Test
        @DisplayName("[State][Positive] 원본과 동일한 상태로 복사된다 (ID/타임스탬프 포함)")
        void copyConstructor_shouldCloneAllFields() {
            User original = newUser();
            User copy = new User(original);

            assertEquals(original.getId(), copy.getId());
            assertEquals(original.getCreatedAt(), copy.getCreatedAt());
            assertEquals(original.getUpdatedAt(), copy.getUpdatedAt());
            assertEquals(original.getNickname(), copy.getNickname());
            assertEquals(original.getEmail(), copy.getEmail());
            assertEquals(original.getPassword(), copy.getPassword());
            assertEquals(original.getRole(), copy.getRole());
            assertEquals(original.getPhoneNumber(), copy.getPhoneNumber());
            assertNotSame(original, copy);
        }
    }

    @Nested
    @DisplayName("BasicEntity updatedAt 제약")
    class UpdatedAtInvariant {

        @Test
        @DisplayName("[Invariant][Negative] updatedAt이 null이면 예외")
        void setUpdatedAt_shouldThrow_whenNull() {
            User u = newUser();
            assertThrows(IllegalArgumentException.class, () -> u.setUpdatedAt(null));
        }

        @Test
        @DisplayName("[Invariant][Negative] 과거 시각으로 설정 시 예외")
        void setUpdatedAt_shouldThrow_whenDecreasing() {
            User u = newUser();
            Long now = u.getUpdatedAt();
            assertThrows(IllegalStateException.class, () -> u.setUpdatedAt(now - 1));
        }

        @Test
        @DisplayName("[Invariant][Positive] 동일/미래 시각은 허용 (현재 구현 기준)")
        void setUpdatedAt_shouldAllow_sameOrFuture() {
            User u = newUser();
            Long now = u.getUpdatedAt();

            u.setUpdatedAt(now);
            assertEquals(now, u.getUpdatedAt());

            u.setUpdatedAt(now + 5_000);
            assertEquals(now + 5_000, u.getUpdatedAt());
        }
    }



}