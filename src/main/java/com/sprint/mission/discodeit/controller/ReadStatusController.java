package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.*;
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
public class ReadStatusController implements ReadStatusApi {

    private final ReadStatusService readStatusService;

    @Override
    @RequestMapping(value = "/readStatuses/{readStatusId}", method = RequestMethod.GET)
    public ResponseEntity<ReadStatusResponseDto> getReadStatus(@PathVariable UUID readStatusId) {
        ReadStatusResponseDto readStatus = readStatusService.getReadStatus(readStatusId);
        return ResponseEntity.ok(readStatus);
    }

    @Override
    @RequestMapping(value = "/readStatuses", method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponseDto> createReadStatus(
            @RequestBody ReadStatusCreateRequestDto request
    ) {
        ReadStatusResponseDto responseDto = readStatusService.createReadStatus(request);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    @RequestMapping(value = "/readStatuses/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<ReadStatusUpdateResponseDto> updateReadStatus(
            @PathVariable UUID id,
            @RequestBody ReadStatusUpdateRequestDto requestDto
    ) {
        ReadStatusUpdateCommand command = ReadStatusUpdateCommand.from(id, requestDto);
        ReadStatusUpdateResponseDto updateResponseDto = readStatusService.updateReadStatus(command);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponseDto);
    }

    @Override
    @RequestMapping(value = "/readStatuses", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatusesByUserId(@RequestParam UUID userId) {
        List<ReadStatusResponseDto> allReadStatusesByUserId = readStatusService.getAllReadStatusesByUserId(userId);
        return ResponseEntity.ok(allReadStatusesByUserId);
    }
}
