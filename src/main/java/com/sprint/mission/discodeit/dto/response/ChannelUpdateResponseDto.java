package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 채널 조회 응답
 * LastMessageAt : 서비스에서 MessageRepository 결과로 계산해서 넣음
 * participantIds : PRIVATE 채널인 경우에만 포함
 */
@Getter
@ToString
@Builder
public class ChannelUpdateResponseDto {

    private UUID id;
    private String name;
    private ChannelType type;
    private Instant createdAt;
    private Instant updatedAt;
    private String description;

    public static ChannelUpdateResponseDto from(Channel channel) {
        return ChannelUpdateResponseDto.builder()
                .id(channel.getId())
                .name(channel.getName())
                .type(channel.getType())
                .createdAt(channel.getCreatedAt())
                .updatedAt(channel.getUpdatedAt())
                .description(channel.getDescription())
                .build();
    }

}
