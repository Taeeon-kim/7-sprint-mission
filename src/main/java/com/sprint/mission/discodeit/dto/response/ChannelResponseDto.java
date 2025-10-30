package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 채널 조회 응답
 * LastMessageAt : 서비스에서 MessageRepository 결과로 계산해서 넣음
 * participantIds : PRIVATE 채널인 경우에만 포함
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResponseDto {

    private UUID channelId;
    private String channelName;
    private ChannelType channelType;
    private Instant createAt;
    private Instant updateAt;
    private Instant lastMessageAt;
    private List<UUID> participantIds;

}
