package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.*;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusApi {

    private final ReadStatusService readStatusService;

    @Override
    @GetMapping("/readStatuses/{readStatusId}")
    public ResponseEntity<ReadStatusResponseDto> getReadStatus(@PathVariable UUID readStatusId) {

        log.debug("읽음 상태 단건 조회 요청 - readStatusId={}", readStatusId);
        ReadStatusResponseDto readStatus = readStatusService.getReadStatus(readStatusId);

        log.debug("읽음 상태 단건 조회 성공 - readStatusId={}", readStatus.id());
        return ResponseEntity.ok(readStatus);
    }

    @Override
    @PostMapping("/readStatuses")
    public ResponseEntity<ReadStatusResponseDto> createReadStatus(
            @Valid @RequestBody ReadStatusCreateRequestDto request
    ) {
        // 생성 요청 자체는 debug
        log.debug(
                "읽음 상태 생성 요청 - userId={}, channelId={}",
                request.userId(),
                request.channelId()
        );
        ReadStatusResponseDto responseDto = readStatusService.createReadStatus(request);

        log.info(
                "읽음 상태 생성 완료 - readStatusId={}, userId={}, channelId={}",
                responseDto.id(),
                responseDto.userId(),
                responseDto.channelId()
        );

        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PatchMapping("/readStatuses/{id}")
    public ResponseEntity<ReadStatusUpdateResponseDto> updateReadStatus(
            @PathVariable UUID id,
            @RequestBody ReadStatusUpdateRequestDto requestDto
    ) {

        log.debug("읽음 상태 수정 요청 - readStatusId={}", id);
        ReadStatusUpdateCommand command = ReadStatusUpdateCommand.from(id, requestDto);
        ReadStatusUpdateResponseDto updateResponseDto = readStatusService.updateReadStatus(command);

        log.info("읽음 상태 수정 완료 - readStatusId={}", id);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponseDto);
    }

    @Override
    @GetMapping("/readStatuses")
    public ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatusesByUserId(@RequestParam UUID userId) {

        log.debug("유저별 읽음 상태 목록 조회 요청 - userId={}", userId);
        List<ReadStatusResponseDto> allReadStatusesByUserId = readStatusService.getAllReadStatusesByUserId(userId);

        return ResponseEntity.ok(allReadStatusesByUserId);
    }
}
