package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        return null;
    }

    //    private final UserRepository userRepository;
//    private final UserStatusRepository userStatusRepository;
//
//    @Override
//    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
//
//        User user = userRepository.findAll().stream()
//                .filter(u->u.getUserId().equals(loginRequestDto.getUserId())
//                && u.getUserPassword().equals(loginRequestDto.getPassword()))
//                .findFirst().orElseThrow(()->new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
//
//        UserStatus status = userStatusRepository.findByUserId(user.getUuid());
//        if(status != null){
//            status.updateLastActiveAt();
//            userStatusRepository.save(status);
//            System.out.println("[유저 정보]");
//            System.out.println("ID: " + user.getUserId());
//            System.out.println("Nickname: " + user.getNickName());
//        }
//
//        return new LoginResponseDto(
//                user.getUserId(),
//                user.getNickName(),
//                user.getEmail(),
//                status != null ? status.getStatus() : null
//        );
//    }
//
//    public void runAuthTest(){
//        System.out.println("-------AuthService-------");
//        loginTest("test00", "pass123");
//        loginTest("test01", "123456p");
//    }
//
//    private void loginTest(String userId,  String password){
//        System.out.println("로그인 시도 : " + userId + " / " + password);
//        try{
//            LoginResponseDto responseDto = login(new LoginRequestDto(userId,password));
//            System.out.println("로그인 성공");
//        } catch(Exception e){
//            System.out.println("로그인 실패" + e.getMessage());
//        }
//    }
}
