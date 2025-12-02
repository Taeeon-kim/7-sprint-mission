package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> binaryContentMap = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        // 메모리 저장
        binaryContentMap.put(binaryContent.getUuid(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID uuid) {
        return binaryContentMap.get(uuid);
    }

    @Override
    public List<BinaryContent> findAllById(UUID uuid) {
        return binaryContentMap.values().stream()
                .filter(content->content.equals((content.getUuid())))
                .toList();
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(binaryContentMap.values());
    }

    @Override
    public void delete(UUID uuid) {
        BinaryContent removed = binaryContentMap.remove(uuid);
    }
}
