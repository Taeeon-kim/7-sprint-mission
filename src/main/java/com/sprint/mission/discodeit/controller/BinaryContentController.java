package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
@Tag(name = "BinaryContent")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{binaryContentId}")
    public BinaryContentResponseDto findById(@PathVariable("binaryContentId") UUID uuid) {
        return binaryContentService.find(uuid);
    }

    @GetMapping
    @Operation(summary = "여러 첨부 파일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공")
    })
    public List<BinaryContentResponseDto> findAllByIdIn() {
        return binaryContentService.findAll();
    }
//    @RequestMapping(method = RequestMethod.GET, params = "userId")
//    public List<BinaryContentResponseDto> getUserBinaryContent(@RequestParam UUID userId){
//        return binaryContentService.findByUserId(userId);
//    }
//
//    @RequestMapping(method = RequestMethod.GET, params = "channelId")
//    public List<BinaryContentResponseDto> getChannelBinaryContent(@RequestParam UUID channelId){
//        return binaryContentService.findByChannelId(channelId);
//    }
}
