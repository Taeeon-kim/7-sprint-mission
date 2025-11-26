package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Tag(name = "Channel")
public class ChannelController {
    private final ChannelService channelService;

    // 공개 채널 생성
    @PostMapping("/public")
    @Operation(summary = "Public Channel 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨")
    })
    public void createPublicChannel(@RequestBody ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {
        channelService.createPublicChannel(channelPublicCreateRequestDto);
    }

    // 비공개 채널 생성
    @PostMapping("/private")
    @Operation(summary = "Private Channel 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨")
    })
    public void createPrivateChannel(@RequestBody ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {
        channelService.createPrivateChannel(channelPrivateCreateRequestDto);
    }

    // 공개 채널 정보 수정
    @PatchMapping("/{channelId}")
    @Operation(summary = "Channel 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨"),
            @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음"),
            @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음")
    })
    public void updateChannel(@PathVariable UUID channelId,
                              @RequestBody ChannelUpdateRequestDto channelUpdateRequestDto) {
        channelService.updateChannel(channelId, channelUpdateRequestDto);
    }

    // 채널 삭제
    @DeleteMapping("/{channelId}")
    @Operation(summary = "Channel 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Channel삭제"),
            @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음")
    })
    public void deleteChannel(@PathVariable UUID uuid) {
        channelService.deleteChannel(uuid);
    }

    // 특정 사용자의 채널 목록 조회
    @GetMapping
    @Operation(summary = "User가 참여 중인 Channel 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공")
    })
    public List<ChannelResponseDto> getChannel(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}
