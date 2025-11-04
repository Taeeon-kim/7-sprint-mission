package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.store.Store;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(prefix = "discodeit.repository",
        name = "type",
        havingValue = "file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        List<UserStatus> allUserStatuses = findAll();
        return allUserStatuses.stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        List<UserStatus> allUserStatuses = findAll();
        return allUserStatuses
                .stream()
                .anyMatch((userStatus) -> userStatus.getUserId().equals(userId));
    }

    @Override
    public Map<UUID, UserStatus> findAllMap() {
        return Store.loadMap(Store.USER_STATUS_DATA_FILE);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Map<UUID, UserStatus> allUserStatuses = findAllMap();
        allUserStatuses.put(userStatus.getId(), userStatus);
        Store.saveMap(Store.USER_STATUS_DATA_FILE, allUserStatuses);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Map<UUID, UserStatus> allMap = findAllMap();

        return Optional.ofNullable(allMap.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        Map<UUID, UserStatus> allUserStatuses = findAllMap();
        UserStatus remove = allUserStatuses.remove(id);
        if (remove != null) {
            Store.saveMap(Store.USER_STATUS_DATA_FILE, allUserStatuses);
            return true;
        }
        throw new IllegalStateException("failed to delete channel");
    }

    @Override
    public List<UserStatus> findAll() {
        Map<UUID, UserStatus> allUserStatuses = Store.loadMap(Store.USER_STATUS_DATA_FILE);
        return allUserStatuses.values()
                .stream()
                .toList();
    }
}
