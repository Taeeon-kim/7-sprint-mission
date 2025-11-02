package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.store.Store;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
@ConditionalOnProperty(prefix = "discodeit.repository",
        name = "type",
        havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    @Override
    public List<BinaryContent> findAllByIds(List<UUID> ids) {
        Map<UUID, BinaryContent> allBinaryContents = findAllMap();
        ids.stream()
                .map(allBinaryContents::get) // // O(N) + O(1) = O(N)
                .filter(Objects::nonNull)
                .toList();
        return List.of();
    }

    @Override
    public Map<UUID, BinaryContent> findAllMap() {
        return Store.loadMap(Store.BINARY_CONTENT_DATA_FILE);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> allBinaryContents = findAllMap();
        allBinaryContents.put(binaryContent.getId(), binaryContent);
        Store.saveMap(Store.BINARY_CONTENT_DATA_FILE, allBinaryContents);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Map<UUID, BinaryContent> allMap = findAllMap();

        return Optional.ofNullable(allMap.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        Map<UUID, BinaryContent> allBinaryContents = findAllMap();
        BinaryContent remove = allBinaryContents.remove(id);
        if (remove != null) {
            Store.saveMap(Store.BINARY_CONTENT_DATA_FILE, allBinaryContents);
            return true;
        }
        throw new IllegalStateException("failed to delete channel");
    }

    @Override
    public List<BinaryContent> findAll() {
        Map<UUID, BinaryContent> allBinaryContents = Store.loadMap(Store.BINARY_CONTENT_DATA_FILE);
        return allBinaryContents.values()
                .stream()
                .toList();
    }
}
