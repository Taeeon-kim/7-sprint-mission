package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> binaryContentMap = new HashMap<>();

    @Override
    public void save(BinaryContent binaryContent) {
        // 메모리 저장
        binaryContentMap.put(binaryContent.getUuid(), binaryContent);
    }

    @Override
    public BinaryContent findById(UUID uuid) {
        return binaryContentMap.get(uuid);
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
    public void delete(UUID uuid) {
        BinaryContent removed = binaryContentMap.remove(uuid);
    }
}
