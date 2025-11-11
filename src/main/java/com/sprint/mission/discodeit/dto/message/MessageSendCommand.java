package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageSendCommand(
        UUID channelId,
        UUID senderId,
        String content,
        List<UUID> binaryFileIds
) {
    public static MessageSendCommand from(MessageSendRequestDto requestDto) {
        return new MessageSendCommand(
                requestDto.channelId(),
                requestDto.senderId(),
                requestDto.content(),
                requestDto.binaryFileIds()
        );
    }
}
