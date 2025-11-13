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

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UUID> createUser(@RequestPart("userCreateRequest") UserSignupRequestDto userSignupRequestDto,
                                           @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        UserSignupCommand command = UserSignupCommand.from(userSignupRequestDto, profile);
        UUID uuid = userService.signUp(command);
        return ResponseEntity.created(URI.create("/api/users/" + uuid)).body(uuid);
    }

    @Override
    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH,  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUser(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequestDto request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        UserUpdateCommand command = UserUpdateCommand.from(userId, request, profile);
        userService.updateUser(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @Override
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
