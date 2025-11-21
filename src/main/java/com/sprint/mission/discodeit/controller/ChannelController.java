package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    // 공개 채널 생성
//    @RequestMapping(value = "/public", method = RequestMethod.POST
    @PostMapping("/public")
    public void createPublicChannel(@RequestBody ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {
        channelService.createPublicChannel(channelPublicCreateRequestDto);
    }

    // 비공개 채널 생성
//    @RequestMapping(value = "/private", method = RequestMethod.POST)
    @PostMapping("/private")
    public void createPrivateChannel(@RequestBody ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {
        channelService.createPrivateChannel(channelPrivateCreateRequestDto);
    }

    // 공개 채널 정보 수정
//    @RequestMapping(method = RequestMethod.PUT, params = "channelId")
    @PatchMapping("/{channelId}")
    public void updateChannel(@PathVariable UUID channelId,
                              @RequestBody ChannelUpdateRequestDto channelUpdateRequestDto) {
        channelService.updateChannel(channelId, channelUpdateRequestDto);
    }

    // 채널 삭제
//    @RequestMapping(method = RequestMethod.DELETE, params = "uuid")
    @DeleteMapping("/{channelId}")
    public void deleteChannel(@PathVariable UUID uuid) {
        channelService.deleteChannel(uuid);
    }

    // 특정 사용자의 채널 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelResponseDto> getChannel(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}
