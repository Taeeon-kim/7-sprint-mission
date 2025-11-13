package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthApi {

    ResponseEntity<UserResponseDto> login(AuthLoginRequestDto authLoginRequestDto);
}
