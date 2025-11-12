package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequestDto request) {
        MessageUpdateCommand command = MessageUpdateCommand.from(request, messageId);
        messageService.updateMessage(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @RequestMapping(value = "/messages", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UUID> sendMessageByChannelId(
            @RequestPart("messageCreateRequest") MessageSendRequestDto request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> files
    ) {
        MessageSendCommand messageSendCommand = MessageSendCommand.from(request, files);
        UUID messageId = messageService.sendMessageToChannel(messageSendCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageId);
    }


    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponseDto>> getAllMessagesByChannelId(
            @RequestParam UUID channelId
    ) {
        List<MessageResponseDto> allMessagesByChannelId = messageService.getAllMessagesByChannelId(channelId);
        return ResponseEntity.ok(allMessagesByChannelId);
    }
}
