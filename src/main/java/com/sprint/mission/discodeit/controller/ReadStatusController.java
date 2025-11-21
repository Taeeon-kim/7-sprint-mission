package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

//    @RequestMapping(method = RequestMethod.POST)
    @PostMapping
    public ReadStatusDto createStatus(@RequestBody ReadStatusCreateRequestDto readStatusCreateRequestDto,
                                      @RequestParam UUID userId,
                                      @RequestParam UUID channelId) {
        readStatusCreateRequestDto.setUserId(userId);
        readStatusCreateRequestDto.setChannelId(channelId);
        return readStatusService.create(readStatusCreateRequestDto);
    }

//    @RequestMapping(method = RequestMethod.PUT)
    @PatchMapping("/{readStatusId}")
    public ReadStatusDto updateStatus(@PathVariable UUID readStatusId,
                                      @RequestBody ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        readStatusUpdateRequestDto.setUuid(readStatusId);
        return readStatusService.update(readStatusUpdateRequestDto);
    }

//    @RequestMapping(method = RequestMethod.GET, params = "userId")
    @GetMapping(params = "userId")
    public List<ReadStatusDto> getReadStatus(@RequestParam UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }
}
