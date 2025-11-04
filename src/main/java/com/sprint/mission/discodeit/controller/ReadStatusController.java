package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/read-statuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UUID> createReadStatus(
            @RequestBody ReadStatusCreateRequestDto request
    ) {
        UUID readStatusId = readStatusService.createReadStatus(request);
        return ResponseEntity.ok(readStatusId);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ReadStatusResponseDto> getReadStatus(@PathVariable UUID readStatusId) {
        ReadStatusResponseDto readStatus = readStatusService.getReadStatus(readStatusId);
        return ResponseEntity.ok(readStatus);
    }


}
