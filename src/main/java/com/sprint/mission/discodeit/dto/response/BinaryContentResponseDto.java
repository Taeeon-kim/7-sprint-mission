package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class BinaryContentResponseDto {

    private UUID messageId;
    private String fileName;
    private long size;
}
