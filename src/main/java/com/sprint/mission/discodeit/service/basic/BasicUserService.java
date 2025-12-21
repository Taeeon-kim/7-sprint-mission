package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    // 레파지토리 의존성 주입
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Transactional
    @Override
    public void createUser(UserCreateRequestDto userCreateRequest) {

        //유저 생성
        if (userRepository.existsByEmail(userCreateRequest.getEmail()) ||
                userRepository.existsByUserName(userCreateRequest.getUserName())) {
            throw new IllegalArgumentException("이미 존재하는 Name 혹은 Email 입니다.");
        }

        // 유저 생성
        User user = new User(
                userCreateRequest.getPassword(),
                userCreateRequest.getEmail(),
                userCreateRequest.getUserName(),
                null
                );

        //프로필 이미지 등록(선택)
        if (userCreateRequest.getProfile() != null
                && !userCreateRequest.getProfile().isEmpty()) {
            try {
                BinaryContent profile = new BinaryContent(
                        userCreateRequest.getProfile().getOriginalFilename(),
                        userCreateRequest.getProfile().getSize(),
                        userCreateRequest.getProfile().getContentType()
//                        userCreateRequest.getProfile().getBytes()
                );
                user.changeProfile(profile);
                binaryContentRepository.save(profile);
            } catch (Exception e) {
                throw new RuntimeException("프로필 처리 중 ERROR", e);
            }
        }

        userRepository.save(user);

        System.out.println("[User 생성 완료] ID: " + user.getUserName() + ", UUID: " + user.getId());
        System.out.println("현재 저장된 유저 수: " + userRepository.findAll().size());
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return UserResponseDto.from(user, user.getUserStatus(), user.getProfile());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> findAllUser() {
        return userRepository.findAll().stream()
                .map(user->UserResponseDto.from(
                        user,
                        user.getUserStatus(),
                        user.getProfile()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateUser(UUID userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userUpdateRequestDto.getUserName() != null && !userUpdateRequestDto.getUserName().isBlank()) {
            user.setUserName(userUpdateRequestDto.getUserName());
        }
        if (userUpdateRequestDto.getEmail() != null && !userUpdateRequestDto.getEmail().isBlank()) {
            user.setEmail(userUpdateRequestDto.getEmail());
        }
        if (userUpdateRequestDto.getNewPassword() != null && !userUpdateRequestDto.getNewPassword().isBlank()) {
            user.setPassword(userUpdateRequestDto.getNewPassword());
        }

        // 프로필 이미지 교체
        if (userUpdateRequestDto.getProfileImage() != null
                && !userUpdateRequestDto.getProfileImage().isEmpty()) {
            try {
                BinaryContent newProfile = new BinaryContent(
                        userUpdateRequestDto.getProfileImage().getOriginalFilename(),
                        userUpdateRequestDto.getProfileImage().getSize(),
                        userUpdateRequestDto.getProfileImage().getContentType()
//                        userUpdateRequestDto.getProfileImage().getBytes()
                );
                user.changeProfile(newProfile);
            } catch (Exception e) {
                throw new RuntimeException("프로필 업로드 중 오류 발생" + e.getMessage());
            }
        }
        System.out.println("[User 수정 완료] : " + user.getId());
    }

    @Transactional
    @Override
    public void deleteUser(UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        System.out.println("삭제 대상 : " + user.getUserName()
                + " | Status : " + user.getUserStatus()
                + " | profile : " + user.getProfile());

        userRepository.delete(user);
    }
}
