package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadStatusDto {
    private UUID uuid; // DTO의 ID
    private UUID userId; // 읽은 유저 식별
    private UUID channelId; // 읽음 상태 적용 채널
    private Instant lastReadAt; // 마지막 메시지 읽은 시각
    private Instant createdAt; // 기록 생성 시간

    public static ReadStatusDto from(ReadStatus readStatus) {
        return ReadStatusDto.builder()
                .uuid(readStatus.getUuid())
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .lastReadAt(Instant.now())
                .createdAt(Instant.now())
                .build();
    }
}
