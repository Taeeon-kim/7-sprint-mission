package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Controller
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UUID> createChannel(@RequestBody ChannelCreateRequestDto request) {
        UUID createdBy = request.userId();
        ChannelCreateCommand cmd = ChannelCreateCommand.from(request);
        UUID channelId = channelService.createChannel(createdBy, cmd);
        return ResponseEntity.created(URI.create("/api/channels/" + channelId)).body(channelId);
    }

}
