package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.AuthRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
//    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = authRepository.findByUserIdAndPassword(
                loginRequestDto.getUserId(),
                loginRequestDto.getPassword()
        ).orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        return LoginResponseDto.from(user);
    }

    //    @Override
//    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
//        User user = userRepository.findAll().stream()
//                .filter(u -> u.getUserId().equals(loginRequestDto.getUserId())
//                        && u.getPassword().equals(loginRequestDto.getPassword()))
//                .findFirst().orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
//
//        return new LoginResponseDto(
//                user.getUserId(),
//                user.getUserName(),
//                user.getEmail()
//        );
//    }
//
//    public void runAuthTest() {
//        System.out.println("-------AuthService-------");
//        loginTest("test00", "pass123");
//        loginTest("test01", "123456p");
//    }
//
//    private void loginTest(String userId, String password) {
//        System.out.printf("로그인 시도 : " + userId + " / " + password + " => ");
//        try {
//            LoginResponseDto responseDto = login(new LoginRequestDto(userId, password));
//            System.out.println("로그인 성공");
//        } catch (Exception e) {
//            System.out.println("로그인 실패" + e.getMessage());
//        }
//    }
}
