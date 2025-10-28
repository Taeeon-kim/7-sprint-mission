package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    @Override
    public void save(BinaryContent binaryContent) {

    }

    @Override
    public BinaryContent findById(UUID uuid) {
        return null;
    }

    @Override
    public List<BinaryContent> findByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<BinaryContent> findByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public List<BinaryContent> delete(UUID uuid) {
        return List.of();
    }
}
