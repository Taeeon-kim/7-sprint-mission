package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;

    @PatchMapping("/user-statuses/{statusId}")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable UUID statusId,
            @RequestBody UserStatusUpdateRequestDto requestDto
    ) {

        log.debug(
                "유저 활동 시각 갱신 요청 - userStatusId={}, newLastActiveAt={}",
                statusId,
                requestDto.newLastActiveAt()
        );

        userStatusService.updateUserStatus(statusId, requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/users/{userId}/userStatus")
    public ResponseEntity<Void> updateUserStatusByUserId(
            @PathVariable UUID userId,
            @Valid @RequestBody UserStatusUpdateRequestDto requestDto
    ) {

        log.debug(
                "유저 활동 시각 갱신 요청 - userId={}, newLastActiveAt={}",
                userId,
                requestDto.newLastActiveAt()
        );

        userStatusService.updateUserStatusByUserId(userId, requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
