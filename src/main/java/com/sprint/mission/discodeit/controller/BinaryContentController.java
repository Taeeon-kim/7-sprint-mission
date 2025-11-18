package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, params = "userId")
    public List<BinaryContentResponseDto> getUserBinaryContent(@RequestParam UUID userId){
        return binaryContentService.findByUserId(userId);
    }

    @RequestMapping(method = RequestMethod.GET, params = "channelId")
    public List<BinaryContentResponseDto> getChannelBinaryContent(@RequestParam UUID channelId){
        return binaryContentService.findByChannelId(channelId);
    }
}
