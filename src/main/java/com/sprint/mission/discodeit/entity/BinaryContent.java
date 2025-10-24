package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String fileName;
    private byte[] bytes;
    private Instant createdAt;
    private String contentType;

    public BinaryContent() {
        this.id = UUID.randomUUID();
    }

}
