package com.sprint.mission.discodeit.integration.fixtures;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.nio.charset.StandardCharsets;

public final class UserFixture {

    private UserFixture() {
    } // 인스턴스 생성 방지

    public static User createUser(UserRepository userRepository, UserStatusRepository userStatusRepository) {
        return userRepository.save(defaultUser());
    }

    public static User createUser(User user, UserRepository userRepository, UserStatusRepository userStatusRepository) {
        return userRepository.save(user);
    }

    public static User createUserWithStatus(UserRepository userRepository,
                                            UserStatusRepository userStatusRepository) {
        User user = userRepository.save(defaultUser());
        UserStatus status = new UserStatus(user);
        user.assignStatus(status);      // 양방향 세팅
        userStatusRepository.save(status);
        return user;
    }

    public static User createUserWithStatus(User user,UserRepository userRepository,
                                            UserStatusRepository userStatusRepository) {
        User saved = userRepository.save(user);
        UserStatus status = new UserStatus(saved);
        saved.assignStatus(status);      // 양방향 세팅
        userStatusRepository.save(status);
        return user;
    }

    public static User defaultUser() {
        byte[] payload = "fake-bytes".getBytes(StandardCharsets.UTF_8);
        BinaryContent binaryContent = new BinaryContent("profile.png", "image/png", (long) payload.length, payload);
        return User.builder()
                .nickname("name")
                .email("ee@exam.com")
                .password("dsfsdfdf")
                .profile(binaryContent)
                .build();
    }
}