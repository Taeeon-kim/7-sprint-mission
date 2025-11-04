package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageSendCommand;
import com.sprint.mission.discodeit.dto.message.MessageSendRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/channels/{channelId}/messages")
@RequiredArgsConstructor
public class ChannelMessageController {

    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UUID> sendMessageByChannelId(
            @PathVariable UUID channelId,
            @RequestBody MessageSendRequestDto request
    ) {
        MessageSendCommand cmd = MessageSendCommand.from(request, channelId);
        UUID messageId = messageService.sendMessageToChannel(cmd);
        return ResponseEntity.ok(messageId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<MessageResponseDto>> getAllMessagesByChannelId(
            @PathVariable UUID channelId
    ) {
        List<MessageResponseDto> allMessagesByChannelId = messageService.getAllMessagesByChannelId(channelId);
        return ResponseEntity.ok(allMessagesByChannelId);
    }
}
