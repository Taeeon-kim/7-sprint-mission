package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;

    @RequestMapping(value = "/user-statuses{statusId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable UUID statusId,
            @RequestBody UserStatusUpdateRequestDto requestDto
    ) {
        userStatusService.updateUserStatus(statusId, requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/users/{userId}/user-statuses", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateUserStatusByUserId(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequestDto requestDto
    ) {
        userStatusService.updateUserStatusByUserId(userId, requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
