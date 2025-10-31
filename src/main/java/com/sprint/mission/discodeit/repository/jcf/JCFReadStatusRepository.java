package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository(Map<UUID, ReadStatus> data) {
        this.data = data;
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public List<ReadStatus> findAll() {
        return data.values()
                .stream()
                .toList();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {

        return data.values()
                .stream()
                .filter((readStatus) -> readStatus.getChannelId().equals(channelId))
                .toList();
    }



    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values()
                .stream()
                .filter((readStatus) -> readStatus.getUserId().equals(userId))
                .toList();

    }
}
