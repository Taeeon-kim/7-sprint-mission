package com.sprint.mission.discodeit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusDto {
    private UUID uuid; // DTO의 ID
    private UUID userId; // 읽은 유저 식별
    private UUID channelId; // 읽음 상태 적용 채널
    private Instant lastReadAt; // 마지막 메시지 읽은 시각
    private Instant createdAt; // 기록 생성 시간
}
