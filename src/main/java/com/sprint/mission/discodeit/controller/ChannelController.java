package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @Override
    @GetMapping("/channels")
    public ResponseEntity<List<ChannelResponseDto>> getAllChannels(@RequestParam(required = false) UUID userId) {
        log.info("채널 목록 조회 요청 userId={}", userId);
        List<ChannelResponseDto> channels = (userId == null) ?
                channelService.getAllChannels() :
                channelService.getAllChannelsByUserId(userId);
        log.debug("채널 목록 조회 성공 userId={} count={}", userId, channels.size());
        return ResponseEntity.ok(channels);
    }

    @Override
    @PostMapping("/channels/public")
    public ResponseEntity<ChannelResponseDto> createChannelPublic(@RequestBody ChannelCreateRequestDto request) { // TODO: @Valid
        log.info("공개 채널 생성 요청 name={}", request.name());
        ChannelCreateCommand cmd = ChannelCreateCommand.from(request, ChannelType.PUBLIC);
        ChannelResponseDto responseDto = channelService.createChannel(cmd);
        log.info("공개 채널 생성 성공 channelId={} name={}", responseDto.id(), responseDto.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    @PostMapping("/channels/private")
    public ResponseEntity<ChannelResponseDto> createChannelPrivate(@RequestBody ChannelCreateRequestDto request) { // TODO: @Valid
        log.info("비공개 채널 생성 요청 name={}", request.name());
        ChannelCreateCommand cmd = ChannelCreateCommand.from(request, ChannelType.PRIVATE);
        ChannelResponseDto responseDto = channelService.createChannel(cmd);
        log.info("비공개 채널 생성 성공 channelId={} name={}", responseDto.id(), responseDto.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    @GetMapping("/channels/{channelId}")
    public ResponseEntity<ChannelResponseDto> getChannel(@PathVariable UUID channelId) {
        log.info("채널 단건 조회 요청 channelId={}", channelId);
        ChannelResponseDto response = channelService.getChannel(channelId);
        log.info("채널 단건 조회 성공 channelId={} name={}", response.id(), response.name());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PatchMapping("/channels/{channelId}")
    public ResponseEntity<Void> updateChannel(
            @PathVariable UUID channelId,
            @RequestBody ChannelUpdateRequestDto request) { // NOTE: patch는 Null 허용이니 valid체크 불필요
        log.info("채널 수정 요청 channelId={}", channelId);
        log.debug("채널 수정 요청 내용 channelId={} request={}", channelId, request);
        channelService.updateChannel(channelId, request);
        log.info("채널 수정 성공 channelId={}", channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @DeleteMapping("/channels/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        log.info("채널 삭제 요청 channelId={}", channelId);
        channelService.deleteChannel(channelId);
        log.info("채널 삭제 성공 channelId={}", channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
