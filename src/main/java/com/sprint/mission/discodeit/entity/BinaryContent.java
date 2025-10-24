package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String fileName;
    private String contentType;
    private Instant createdAt;
    private byte[] bytes;

    public BinaryContent() {
        this.id = UUID.randomUUID();
    }

    public BinaryContent(String fileName, String contentType, byte[] bytes) {
        this();
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
        this.createdAt = Instant.now();
    }
}
