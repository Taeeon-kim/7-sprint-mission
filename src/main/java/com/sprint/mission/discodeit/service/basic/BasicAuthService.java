package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.AuthRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final AuthRepository authRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = authRepository.findByUserIdAndPassword(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()
        ).orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        return LoginResponseDto.from(user);
    }
}
