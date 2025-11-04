package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/users/{userId}/read-statuses")
@Controller
@RequiredArgsConstructor
public class ReadStatusByUserController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatusesByUserId(@PathVariable UUID userId) {
        List<ReadStatusResponseDto> allReadStatusesByUserId = readStatusService.getAllReadStatusesByUserId(userId);
        return ResponseEntity.ok(allReadStatusesByUserId);
    }
}
