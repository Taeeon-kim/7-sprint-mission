package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping("/messages/{messageId}")
    public void getMessageById() {

    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<Void> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequestDto request) {
        MessageUpdateCommand command = MessageUpdateCommand.from(request, messageId);
        messageService.updateMessage(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @RequestMapping(value = "/channels/{channelId}/messages", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UUID> sendMessageByChannelId(
            @PathVariable UUID channelId,
            @RequestBody MessageSendRequestDto request
    ) {
        MessageSendCommand cmd = MessageSendCommand.from(request, channelId);
        UUID messageId = messageService.sendMessageToChannel(cmd);
        return ResponseEntity.ok(messageId);
    }


    @RequestMapping(value = "/channels/{channelId}/messages", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<MessageResponseDto>> getAllMessagesByChannelId(
            @PathVariable UUID channelId
    ) {
        List<MessageResponseDto> allMessagesByChannelId = messageService.getAllMessagesByChannelId(channelId);
        return ResponseEntity.ok(allMessagesByChannelId);
    }
}
