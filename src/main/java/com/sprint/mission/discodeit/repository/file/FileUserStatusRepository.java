package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    // 메모리 저장소
    private final Map<UUID, UserStatus> statusMap = new HashMap<>();

    // 파일 저장 경로
    private static final String FILE_PATH = "data/user_status.ser";

    @Override
    public void save(UserStatus userStatus) {
        statusMap.put(userStatus.getUuid(), userStatus);
//        saveToFile();
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return null;
    }

    @Override
    public List<UserStatus> findAll() {
        return List.of();
    }

    @Override
    public void updateLastActiveAt(UUID userId) {

    }

    @Override
    public void delete(UUID uuid) {
        UserStatus removed = statusMap.remove(uuid);
        if (removed != null) {
            System.out.println("[상태 삭제] : " + removed.getStatus());
        }
    }
}
