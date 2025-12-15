package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Entity
@Table(name = "binary_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName; //첨부파일 이름

    @Column(nullable = false)
    private Long size;

    @Column(name = "content_type", length = 100, nullable = false)
    private String contentType; //파일 타입

    @Column(columnDefinition = "BYTEA", nullable = false)
    private byte[] bytes; //파일 내 실제 데이터

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        super();
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
