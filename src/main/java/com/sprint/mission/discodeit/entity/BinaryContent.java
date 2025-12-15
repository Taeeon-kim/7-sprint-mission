package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Getter
@ToString
public class BinaryContent extends  BaseEntity{

    private String fileName; //첨부파일 이름
    private String contentType; //파일 타입
    private byte[] bytes; //파일 내 실제 데이터

    public BinaryContent(String fileName, String contentType, byte[] bytes) {
        super();
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
