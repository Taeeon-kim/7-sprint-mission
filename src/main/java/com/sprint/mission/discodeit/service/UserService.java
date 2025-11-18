package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 회원가입
    UserResponseDto signUp(UserSignupCommand signupCommand);

    // 회원정보 읽기
    UserResponseDto getUserById(UUID userId);

    // 회원탈퇴
    void deleteUser(UUID userId);

    // 정보수정
    UserResponseDto updateUser(UserUpdateCommand updateCommand);

    // 모든 유저리스트 읽기(관리측면 메서드)
    List<UserDto> getAllUsers();

    List<User> getUsersByIds(List<UUID> userIds);
}
