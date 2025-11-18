package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class BinaryContentResponseDto {

    private UUID messageId;
    private String fileName;
    private long size;

    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return BinaryContentResponseDto.builder()
                .messageId(binaryContent.getUuid())
                .fileName(binaryContent.getFileName())
                .size(binaryContent.getBytes().length)
                .build();
    }
}
