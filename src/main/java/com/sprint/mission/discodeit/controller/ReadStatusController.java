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
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ReadStatusDto createStatus(@RequestBody ReadStatusCreateRequestDto readStatusCreateRequestDto,
                                      @RequestParam UUID userId,
                                      @RequestParam UUID channelId) {
        readStatusCreateRequestDto.setUserId(userId);
        readStatusCreateRequestDto.setChannelId(channelId);
        return readStatusService.create(readStatusCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ReadStatusDto updateStatus(@RequestParam UUID readStatusId,
                                      @RequestBody ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        readStatusUpdateRequestDto.setUuid(readStatusId);
        return readStatusService.update(readStatusUpdateRequestDto);
    }

    @RequestMapping(method = RequestMethod.GET, params = "userId")
    public List<ReadStatusDto> getReadStatus(@RequestParam UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }
}
