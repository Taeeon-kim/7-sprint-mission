package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.MessageService;
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
public class MessageController {
    private final MessageService messageService;
    private final ChannelRepository channelRepository;

    // 메시지 전송(저장)
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public MessageResponseDto createMessage(@ModelAttribute MessageCreateRequestDto messageCreateRequestDto,
                                            @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        System.out.println("files: " + files);
        var message = messageService.createMessage(messageCreateRequestDto, files);
        return MessageResponseDto.from(message);
    }

    // 메시지 수정
    @RequestMapping(method = RequestMethod.PUT, consumes = "multipart/form-data")
    public MessageResponseDto updateMessage(@ModelAttribute("data") MessageUpdateRequestDto messageUpdateRequestDto,
                                            /*@RequestPart(value = "file", required = false)*/ List<MultipartFile> files) {
        var updateMessage = messageService.updateMessage(messageUpdateRequestDto, files);
        return MessageResponseDto.from(updateMessage);
    }

    // 메시지 삭제
    @RequestMapping(method = RequestMethod.DELETE, params = "uuid")
    public void deleteMessage(@RequestParam UUID uuid) {
        messageService.deleteMessage(uuid);
    }

    // 특정 채널 메시지 목록 조회
    @RequestMapping(method = RequestMethod.GET, params = "channelId")
    public List<MessageResponseDto> getMessageByChannel(@RequestParam UUID channelId) {
        var channel = channelRepository.findByChannel(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
        return messageService.findChannelAllMessage(channel)
                .stream().map(MessageResponseDto::from)
                .toList();
    }
}
