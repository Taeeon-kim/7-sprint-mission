package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true // 기본값: jcf
)
public class JCFReadStatusRepository implements ReadStatusRepository {

    // 임시저장
    private final Map<UUID, ReadStatus> store = new HashMap<>();

    @Override
    public void save(ReadStatus readStatus) {
        store.put(readStatus.getUuid(), readStatus);
    }

    @Override
    public ReadStatus findByUserAndChannel(UUID userId, UUID channelId) {
        return store.values().stream()
                .filter(rs -> rs.getUserId().equals(userId)
                        && rs.getChannelId().equals(channelId))
                .findFirst().orElse(null);
    }

    @Override
    public ReadStatus findById(UUID uuid) {
        return store.get(uuid);
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return store.values().stream()
                .filter(rs->rs.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return store.values().stream()
                .filter(rs->rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void update(ReadStatus readStatus) {
        store.put(readStatus.getUuid(), readStatus);
    }

    @Override
    public void delete(UUID uuid) {
        store.remove(uuid);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        List<UUID> toRemove = store.values().stream()
                .filter(rs->rs.getChannelId().equals(channelId))
                .map(rs->rs.getUuid())
                .collect(Collectors.toList());
        toRemove.forEach(store::remove);
    }
}
