package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record MessageResponseDto(
        UUID id,
        UUID channelId,
        UserResponseDto author,
        String content,
        List<BinaryContentResponseDto> attachments,
        Instant createdAt,
        Instant updatedAt

) {
}
