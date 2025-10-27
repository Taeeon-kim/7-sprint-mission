package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

/**
 * 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
 * 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
 * 수정 불가능한 도메인 모델로 간주함, 따라서 updateAt필드는 정의하지 않음
 * User, Message 도메인 모델과의 의존 관계 방향성을 고려해 id 참조 필드 추가
 */
@Getter @Setter @ToString
public class BinaryContent {

    private final UUID uuid;
    private final UUID userId; // 유저 프로필
    private final UUID messageId; // 메시지 첨부 파일
    private final byte[] data; // 실제 데이터 파일
    private final String fileName; // 파일 이름
    private final String contentType; // 이미지 타입
    private final Instant createdAt; // 생성 시간

    public BinaryContent(UUID uuid, UUID userId, UUID messageId, byte[] data, String fileName, String contentType, Instant createdAt) {
        this.uuid = uuid;
        this.userId = userId;
        this.messageId = messageId;
        this.data = data;
        this.fileName = fileName;
        this.contentType = contentType;
        this.createdAt = createdAt;
    }
}
