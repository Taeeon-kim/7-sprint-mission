package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.MessageApi;
import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @Override
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<MessageUpdateResponseDto> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequestDto request) {
        MessageUpdateCommand command = MessageUpdateCommand.from(request, messageId);
        messageService.updateMessage(command);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping(value = "/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponseDto> sendMessageByChannelId(
            @RequestPart("messageCreateRequest") MessageSendRequestDto request, // TODO: @Valid
            @RequestPart(value = "attachments", required = false) List<MultipartFile> files
    ) {
        MessageSendCommand messageSendCommand = MessageSendCommand.from(request, files);
        MessageResponseDto responseDto = messageService.sendMessageToChannel(messageSendCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    @GetMapping("/messages")
    public ResponseEntity<PageResponse<MessageResponseDto>> getAllMessagesByChannelId(
            @RequestParam UUID channelId,
            Pageable pageable, // Pageable 명시에 놓으면 PageableHandlerMethodArgumentResolver 를통해 프론트에서 파라미터 형식 이름만 맞추면 바인딩해줌
            @RequestParam(required = false) Instant cursor
            ) {
        PageResponse<MessageResponseDto> allMessagesByChannelId = messageService.getAllMessagesByChannelId(channelId, pageable, cursor);
        return ResponseEntity.ok(allMessagesByChannelId);
    }
}
