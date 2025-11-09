package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    // 메모리 저장소
    private final Map<UUID, UserStatus> statusMap = new HashMap<>();

    // 파일 저장 경로
//    private static final String FILE_PATH = "data/user_status.ser";

    @Override
    public void save(UserStatus userStatus) {
        statusMap.put(userStatus.getUserId(), userStatus);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return statusMap.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst().orElse(null);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(statusMap.values());
    }

    @Override
    public void delete(UUID uuid) {
        statusMap.remove(uuid);
    }
}
