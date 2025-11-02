package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data = new HashMap<>();

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
