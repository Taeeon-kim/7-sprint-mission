package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
 * 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
 * 수정 불가능한 도메인 모델로 간주함, 따라서 updateAt필드는 정의하지 않음
 * User, Message 도메인 모델과의 의존 관계 방향성을 고려해 id 참조 필드 추가
 */
@Getter @ToString
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
