package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BasicEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent extends BasicEntity {
    private String fileName;
    private String contentType;
    private Instant createdAt;
    private byte[] bytes;
    
    @Builder
    public BinaryContent(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
        this.createdAt = Instant.now();
    }
}
