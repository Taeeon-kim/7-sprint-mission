package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.message.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageApi {

    ResponseEntity<MessageUpdateResponseDto> updateMessage(UUID messageId, MessageUpdateRequestDto request);

    ResponseEntity<Void> deleteMessage(UUID messageId);

    ResponseEntity<MessageResponseDto> sendMessageByChannelId(MessageSendRequestDto request, List<MultipartFile> files);

    ResponseEntity<List<MessageResponseDto>> getAllMessagesByChannelId(UUID channelId);
}
