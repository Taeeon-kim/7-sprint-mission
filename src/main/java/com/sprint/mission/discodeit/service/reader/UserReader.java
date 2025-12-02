package com.sprint.mission.discodeit.service.reader;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

// User 헬퍼
@Component
public class UserReader {

    private final UserRepository userRepository;

    public UserReader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserOrThrow(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자가 없습니다"));
    }

    public List<User> findUsersByIds(List<UUID> userIds) {
        if (userIds == null) { // NOTE: 어디서 불를지 모르기때문에 불변식은 아니더래도 자기방어를 위한 방어코드 추가
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return userRepository.findAllById(userIds);
    }
}
