package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
//        return authService.login(loginRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(loginRequestDto));
    }
}
