package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository implements ReadStatusRepository {
    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        List<ReadStatus> allReadStatuses = findAll();
        return allReadStatuses.stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> allReadStatuses = findAll();
        return allReadStatuses.stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public Map<UUID, ReadStatus> findAllMap() {
        return Store.loadMap(Store.READ_STATUS_DATA_FILE);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> allReadStatuses = findAllMap();
        allReadStatuses.put(readStatus.getId(), readStatus);
        Store.saveMap(Store.READ_STATUS_DATA_FILE, allReadStatuses);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Map<UUID, ReadStatus> allMap = findAllMap();

        return Optional.ofNullable(allMap.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        Map<UUID, ReadStatus> allReadStatuses = findAllMap();
        ReadStatus remove = allReadStatuses.remove(id);
        if (remove != null) {
            Store.saveMap(Store.READ_STATUS_DATA_FILE, allReadStatuses);
            return true;
        }
        throw new IllegalStateException("failed to delete channel");
    }

    @Override
    public List<ReadStatus> findAll() {
        Map<UUID, ReadStatus> allReadStatuses = Store.loadMap(Store.READ_STATUS_DATA_FILE);
        return allReadStatuses.values()
                .stream()
                .toList();
    }
}
