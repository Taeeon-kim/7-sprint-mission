package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UUID> createUser(@RequestBody UserSignupRequestDto userSignupRequestDto) {
        UUID uuid = userService.signUp(userSignupRequestDto);
        return ResponseEntity.ok(uuid);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    @ResponseBody
    public void updateUser(
            @PathVariable UUID userId,
            @RequestBody UserUpdateRequestDto request
    ) {
        userService.updateUser(userId, request);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

}
