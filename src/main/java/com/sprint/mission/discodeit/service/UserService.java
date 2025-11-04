package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 회원가입
    UUID signUp(UserSignupRequestDto userSignupRequestDto);

    // 회원정보 읽기
    UserResponseDto getUserById(UUID userId);

    // 회원탈퇴
    void deleteUser(UUID userId);

    // 정보수정
    void updateUser(UUID id, UserUpdateRequestDto userUpdateRequestDto);

    // 모든 유저리스트 읽기(관리측면 메서드)
    List<User> getAllUsers();

    List<User> getUsersByIds(List<UUID> userIds);
}
