package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageUpdateCommand;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping("/{messageId}")
    public void getMessageById() {

    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<Void> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequestDto request) {
        MessageUpdateCommand command = MessageUpdateCommand.from(request, messageId);
        messageService.updateMessage(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
