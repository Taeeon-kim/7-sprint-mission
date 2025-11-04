package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/users/{userId}/user-status")
@RequiredArgsConstructor
public class UserStatusByUserController {

    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<Void> updateUserStatusByUserId(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequestDto requestDto
    ) {
        userStatusService.updateUserStatusByUserId(userId, requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
