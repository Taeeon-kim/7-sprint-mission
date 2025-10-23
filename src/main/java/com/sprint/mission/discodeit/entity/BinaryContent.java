package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String fileName;
    private byte[] bytes;
    private Instant createdAt;
    private String contentType; // TODO: enum으로 필요한지


}
