package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateCommand;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/readStatuses/{readStatusId}", method = RequestMethod.GET)
    public ResponseEntity<ReadStatusResponseDto> getReadStatus(@PathVariable UUID readStatusId) {
        ReadStatusResponseDto readStatus = readStatusService.getReadStatus(readStatusId);
        return ResponseEntity.ok(readStatus);
    }

    @RequestMapping(value = "/readStatuses", method = RequestMethod.POST)
    public ResponseEntity<UUID> createReadStatus(
            @RequestBody ReadStatusCreateRequestDto request
    ) {
        UUID readStatusId = readStatusService.createReadStatus(request);
        return ResponseEntity.ok(readStatusId);
    }

    @RequestMapping(value = "/readStatuses/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateReadStatus(
            @PathVariable UUID id,
            @RequestBody ReadStatusUpdateRequestDto requestDto
    ) {
        ReadStatusUpdateCommand command = ReadStatusUpdateCommand.from(id, requestDto);
        readStatusService.updateReadStatus(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @RequestMapping(value = "/readStatuses", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatusesByUserId(@RequestParam UUID userId) {
        List<ReadStatusResponseDto> allReadStatusesByUserId = readStatusService.getAllReadStatusesByUserId(userId);
        return ResponseEntity.ok(allReadStatusesByUserId);
    }
}
