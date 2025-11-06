package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true)
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data = new ConcurrentHashMap<>();

    @Override
    public BinaryContent save(BinaryContent content) {
        data.put(content.getId(), content);
        return content;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public List<BinaryContent> findAll() {
        return data.values()
                .stream()
                .toList();
    }

    @Override
    public List<BinaryContent> findAllByIds(List<UUID> ids) {
        return ids.stream()
                .map((id)-> data.get(id)) // // O(N) + O(1) = O(N)
                .filter((binaryContent)-> Objects.nonNull(binaryContent))
                .toList();
    }

    @Override
    public Map<UUID, BinaryContent> findAllMap() {
        return data;
    }
}
