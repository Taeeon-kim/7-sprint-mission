package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.AuthApi;
import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserResponseDto> login(@RequestBody AuthLoginRequestDto authLoginRequestDto) {

        UserResponseDto responseDto = authService.login(authLoginRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}
