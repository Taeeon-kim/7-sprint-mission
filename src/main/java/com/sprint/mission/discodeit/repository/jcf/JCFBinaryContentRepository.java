package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

//    public List<BinaryContent> findByInputId(UUID inputId){
//        return binaryContentMap.values().stream()
//                .filter(bc->bc.equals(bc.getInputId()))
//                .toList();
//    }

    @Override
    public void delete(UUID uuid) {
        BinaryContent removed = binaryContentMap.remove(uuid);
    }
}
