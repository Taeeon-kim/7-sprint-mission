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

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    // 레파지토리 의존성 주입
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public void createUser(UserCreateRequestDto userCreateRequestDto) {
        //유저 생성
        boolean exists = userRepository.findAll().stream()
                .anyMatch(user -> userCreateRequestDto.getUserId().equals(user.getUserId())
                        || userCreateRequestDto.getUserName().equals(user.getUserName())
                        || userCreateRequestDto.getEmail().equals(user.getEmail()));
        if (exists) {
            throw new IllegalStateException("이미 존재하는 ID 혹은 Name 혹은 Email 입니다.");
        }

        String userId = userCreateRequestDto.getUserId();
        String userName = userCreateRequestDto.getUserName();
        String userPassword = userCreateRequestDto.getPassword();
        String email = userCreateRequestDto.getEmail();
        User user = new User(userId, userPassword, email, userName, null);
        userRepository.save(user);

        //프로필 이미지 등록(선택)
        if (userCreateRequestDto.getProfileImagePath() != null
                && !userCreateRequestDto.getProfileImagePath().isEmpty()) {
            try{
                BinaryContent profile = new BinaryContent(
//                        UUID.randomUUID(),
//                        Instant.now(),
                        userCreateRequestDto.getProfileImagePath().getOriginalFilename(),
                        userCreateRequestDto.getProfileImagePath().getContentType(),
                        userCreateRequestDto.getProfileImagePath().getBytes()
                );
                binaryContentRepository.save(profile);
                user.setProfileImageId(profile.getUuid());
            } catch(Exception e){
                userCreateRequestDto.setProfileImagePath(null);
            }
        }
        // 유저 상태 생성
        UserStatus status = new UserStatus(user.getUuid());
        userStatusRepository.save(status);

        userRepository.save(user);
        System.out.println("[User 생성 완료] ID: " + user.getUserId() + ", UUID: " + user.getUuid());
        System.out.println("현재 저장된 유저 수: " + userRepository.findAll().size());

//        System.out.println("[User 생성 완료] : " + user.getUserId());
    }

    @Override
    public UserResponseDto findById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserStatus status = new UserStatus(user.getUuid());
        BinaryContent profileContent = null;
        if (user.getProfileImageId() != null) {
            profileContent = binaryContentRepository.findById(user.getProfileImageId());
        }
//        if (profileContent == null) {
//            profileContent = new BinaryContent(
//                    UUID.randomUUID(),
//                    Instant.now(),
//                    "",
//                    "image/png",
//                    new byte[0]
//            );
//        }
        return UserResponseDto.from(user, status, profileContent);
    }

    @Override
    public List<UserResponseDto> findAllUser() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserStatus status = userStatusRepository.findByUserId(user.getUuid());
                    BinaryContent profileContent = null;
                    if (user.getProfileImageId() != null) {
                        profileContent = binaryContentRepository.findById(user.getProfileImageId());
                    }
                    if (profileContent == null) {
                        profileContent = new BinaryContent(
//                                UUID.randomUUID(),
//                                Instant.now(),
                                null,
                                "image/png",
                                new byte[0]
                        );
                    }
                    return UserResponseDto.from(user, status, profileContent);
                }).collect(Collectors.toList());
    }

    @Override
    public void updateUser(String userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userUpdateRequestDto.getUserName() != null && !userUpdateRequestDto.getUserName().isBlank()) {
            user.setUserName(userUpdateRequestDto.getUserName());
        }

        if (userUpdateRequestDto.getEmail() != null && !userUpdateRequestDto.getEmail().isBlank()) {
            user.setEmail(userUpdateRequestDto.getEmail());
        }

        // 프로필 이미지 교체
        if (userUpdateRequestDto.getProfileImage() != null
                && !userUpdateRequestDto.getProfileImage().isEmpty()) {
            try {
                UUID existingProfileId = user.getProfileImageId(); // 기존 프로필 UUID
                BinaryContent newProfile = new BinaryContent(
//                        UUID.randomUUID(),
//                        Instant.now(),
                        userUpdateRequestDto.getProfileImage().getOriginalFilename(),
                        userUpdateRequestDto.getProfileImage().getContentType(),
                        userUpdateRequestDto.getProfileImage().getBytes()
                );
                binaryContentRepository.save(newProfile);
//
//                // user의 profileImageId 업데이트
//                user.setProfileImageId(newProfile.getUuid());

                // 기존 프로필 삭제
                if (existingProfileId != null) {
                    try {
                        binaryContentRepository.delete(existingProfileId);
                    } catch (Exception e) {
                        System.out.println("기존 프로필 삭제 중 문제 발생 : " + e.getMessage());
                    }
                }
                user.setProfileImageId(newProfile.getUuid());
            } catch (Exception e) {
                throw new RuntimeException("프로필 업로드 중 오류 발생" + e.getMessage());
            }
        }

        if (userUpdateRequestDto.getNewPassword() != null && !userUpdateRequestDto.getNewPassword().isBlank()) {
            user.setPassword(userUpdateRequestDto.getNewPassword());
        }

        userRepository.save(user);
        System.out.println("[User 수정 완료] : " + user.getUserId());
    }

    @Override
    public void deleteUser(UUID uuid) {
        User user = userRepository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 상태 삭제
        UserStatus status = userStatusRepository.findByUserId(uuid);
        if (status != null) {
            userStatusRepository.delete(uuid);
        }

        //프로필 삭제
        if (user.getProfileImageId() != null) {
            binaryContentRepository.delete(user.getProfileImageId());
        }
        System.out.println("삭제 대상 : " + user.getUserId()
                + " | Status : " + status.getStatus()
                + " | profile : " + user.getProfileImageId());

        userRepository.delete(uuid);
    }

    public void runTest() {
        // User 등록
        UserCreateRequestDto[] userCreateRequestDtos = new UserCreateRequestDto[]{
                new UserCreateRequestDto("test00", "alice123@gmail.com", "pass123", "Alice", null, null),
                new UserCreateRequestDto("test02", "name000@gmail.com", "0000pass", "Bob", null, null),
                new UserCreateRequestDto("test03", "chilysource@gmail.com", "12341234", "Chily", null, null),
                new UserCreateRequestDto("test05", "tomtom00@gmail.com", "pw123456", "Tom", null, null)
        };

        //유저 생성
        for (UserCreateRequestDto userCreateRequestDto : userCreateRequestDtos) {
            createUser(userCreateRequestDto);
        }

        // 전체 조회
        userList();

        // 유저 수정
        User userUpdate = userRepository.findAll().stream()
                .filter(u -> u.getUserId().equals("test02"))
                .findFirst().orElseThrow(() -> new RuntimeException("수정할 유저를 찾을 수 없습니다."));
//        updateUser(userUpdate.getUserId(), new UserUpdateRequestDto("Minsu", "", "0123456pw", "고양이.png"));

        // 수정 후 단건 조회
        UserResponseDto userResponseDto = findById(userUpdate.getUserId());
        System.out.println("수정된 유저 : " + userResponseDto.getUserId()
                + " / Name : " + userResponseDto.getUserName()
                + " / Status : " + userResponseDto.getUserStatus()
                + " / profile : " + userResponseDto.getProfileImagePath());
        userList();

        // 유저 삭제
        User deleteUser = userRepository.findAll().stream()
                .filter(u -> u.getUserId().equals("test02"))
                .findFirst().orElseThrow();
        deleteUser(deleteUser.getUuid());

        // 전체 조회
        userList();
    }

    //유저 전체 조회
    public void userList() {
        System.out.println("[유저 전체 조회]");
        Set<String> userSet = new HashSet<>();
        for (UserResponseDto u : findAllUser()) {
            if (userSet.add(u.getUserId())) { // userId 기준
                System.out.println("ID: " + u.getUserId() + " | Name: " + u.getUserName()
                        + " | Status : " + u.getUserStatus() + "| Profile :" + u.getProfileImagePath());
            }
        }
    }
}
