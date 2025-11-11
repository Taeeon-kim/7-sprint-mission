package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true // 기본값: jcf
)
public class JCFUserStatusRepository implements UserStatusRepository {

    // 메모리 저장소
    private final Map<UUID, UserStatus> statusMap = new HashMap<>();

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
