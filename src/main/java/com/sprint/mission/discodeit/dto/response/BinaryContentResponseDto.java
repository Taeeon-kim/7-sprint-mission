package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class BinaryContentResponseDto {

//    private UUID messageId;
    private UUID id;
    private String fileName;
    private String contentType;
    private long size;
    private long bytes;
    private Instant createdAt;

    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return BinaryContentResponseDto.builder()
//                .messageId(binaryContent.getUuid())
                .id(binaryContent.getUuid())
                .contentType(binaryContent.getContentType())
                .fileName(binaryContent.getFileName())
                .size(binaryContent.getBytes().length)
                .bytes(binaryContent.getBytes().length)
                .createdAt(binaryContent.getCreateAt())
                .build();
    }
}
