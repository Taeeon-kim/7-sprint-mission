package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.UserApi;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> createUser(@RequestPart("userCreateRequest") UserSignupRequestDto userSignupRequestDto, // TODO: @Valid
                                                      @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        UserSignupCommand command = UserSignupCommand.from(userSignupRequestDto, profile);
        UserResponseDto responseDto = userService.signUp(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequestDto request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        UserUpdateCommand command = UserUpdateCommand.from(userId, request, profile);
        UserResponseDto userResponseDto = userService.updateUser(command);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @Override
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
