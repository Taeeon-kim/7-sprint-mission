package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Message")
public class MessageController {
    private final MessageService messageService;
    private final ChannelRepository channelRepository;

    // 메시지 전송(저장)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Message 생성", operationId = "create_2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨"),
            @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음")
    })
    public MessageResponseDto createMessage(@ModelAttribute MessageCreateRequestDto messageCreateRequestDto,
                                            @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        System.out.println("files: " + files);
        var message = messageService.createMessage(messageCreateRequestDto, files);
        return MessageResponseDto.from(message);
    }

    // 메시지 수정
    @PatchMapping(value = "/{messageId}", consumes = "multipart/form-data")
    @Operation(summary = "Message 내용 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨"),
            @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
    })
    public MessageResponseDto updateMessage(
            @ModelAttribute("data") MessageUpdateRequestDto messageUpdateRequestDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        var updateMessage = messageService.updateMessage(messageUpdateRequestDto, files);
        return MessageResponseDto.from(updateMessage);
    }

    // 메시지 삭제
    @DeleteMapping("/{messageId}")
    @Operation(summary = "Message 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
            @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
    })
    public void deleteMessage(@PathVariable UUID uuid) {
        messageService.deleteMessage(uuid);
    }

    // 특정 채널 메시지 목록 조회
    @GetMapping
    @Operation(summary = "Channel의 Message 목록 조회", operationId = "findAllByChannelId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
    })
    public List<MessageResponseDto> getMessageByChannel(@Parameter(description = "조회할 Channel ID")
                                                        @RequestParam UUID channelId) {
        var channel = channelRepository.findByChannel(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
        return messageService.findChannelAllMessage(channel)
                .stream().map(MessageResponseDto::from)
                .toList();
    }
}
