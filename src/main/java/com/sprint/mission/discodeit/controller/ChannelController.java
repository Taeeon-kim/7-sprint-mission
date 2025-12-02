package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @Override
    @GetMapping("/channels")
    public ResponseEntity<List<ChannelResponseDto>> getAllChannels(@RequestParam(required = false) UUID userId) {
        List<ChannelResponseDto> channels = (userId == null) ?
                channelService.getAllChannels() :
                channelService.getAllChannelsByUserId(userId);
        return ResponseEntity.ok(channels);
    }

    @Override
    @PostMapping("/channels/public")
    public ResponseEntity<ChannelResponseDto> createChannelPublic(@RequestBody ChannelCreateRequestDto request) { // TODO: @Valid
        ChannelCreateCommand cmd = ChannelCreateCommand.from(request, ChannelType.PUBLIC);
        ChannelResponseDto responseDto = channelService.createChannel(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    @PostMapping("/channels/private")
    public ResponseEntity<ChannelResponseDto> createChannelPrivate(@RequestBody ChannelCreateRequestDto request) { // TODO: @Valid
        ChannelCreateCommand cmd = ChannelCreateCommand.from(request, ChannelType.PRIVATE);
        ChannelResponseDto responseDto = channelService.createChannel(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    @GetMapping("/channels/{channelId}")
    public ResponseEntity<ChannelResponseDto> getChannel(@PathVariable UUID channelId) {
        ChannelResponseDto response = channelService.getChannel(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PatchMapping("/channels/{channelId}")
    public ResponseEntity<Void> updateChannel(
            @PathVariable UUID channelId,
            @RequestBody ChannelUpdateRequestDto request) { // NOTE: patch는 Null 허용이니 valid체크 불필요
        channelService.updateChannel(channelId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @DeleteMapping("/channels/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
