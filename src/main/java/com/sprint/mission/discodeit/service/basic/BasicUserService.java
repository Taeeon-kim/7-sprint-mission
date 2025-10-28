package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserReponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    // 레파지토리 의존성 주입
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    //CRUD
    @Override
    public void createUser(UserCreateRequestDto userCreateRequestDto) {
        //중복검사
        boolean exists = userRepository.findAll().stream()
                .anyMatch(u -> u.getUserId().equals(userCreateRequestDto.getUserId())
                        || u.getEmail().equals(userCreateRequestDto.getEmail())
                        || u.getNickName().equals(userCreateRequestDto.getNickName()));
        if (exists) {
            throw new IllegalStateException("이미 존재하는 Id 또는 nickname 또는 email 입니다.");
//            System.out.println("이미 존재하는 Id 또는 nickname 또는 email 입니다.");
        }

        // user생성
        User user = new User(
                userCreateRequestDto.getUserId(),
                userCreateRequestDto.getEmail(),
                userCreateRequestDto.getPassword(),
                userCreateRequestDto.getNickName(),
                null
        );
        userRepository.save(user);

        // 유저 상태 생성
        UserStatus status = new UserStatus(user.getUuid());
        userStatusRepository.save(status);

        //프로필 이미지 등록(선택)
        if (userCreateRequestDto.getProfileImagePath() != null
                && !userCreateRequestDto.getProfileImagePath().isBlank()) {
            BinaryContent profile = new BinaryContent(
                    user.getUuid(),
                    null,
                    userCreateRequestDto.getProfileImagePath(),
                    "image/png"
            );
            binaryContentRepository.save(profile);

            // user의 profileImageId 필드에 BinaryContent.uuid 연결
            user.setProfileImage(profile.getUuid());
            userRepository.save(user); // 업데이트 저장
        }
        System.out.println("[User 생성 완료] : " + user.getUserId());
    }

    @Override
    public UserReponseDto readUser(UUID uuid) {
        User user = userRepository.findById(uuid);
        if (user == null) return null;

        UserStatus status = userStatusRepository.findByUserId(uuid);
        BinaryContent profile = (user.getProfileImageId() != null)
                ? binaryContentRepository.findById(user.getProfileImageId())
                : null;
        return new UserReponseDto(
                user.getUuid(),
                user.getNickName(),
                user.getEmail(),
                user.getNickName(),
                (profile != null ? profile.getFilePath() : null),
                (profile != null ? profile.getContentType() : null),
                (status != null && status.isOnline()),
                user.getCreateAt()
        );
    }

    @Override
    public List<UserReponseDto> readAllUser() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserStatus status = userStatusRepository.findByUserId(user.getUuid());
                    BinaryContent profile = (user.getProfileImageId() != null)
                            ? binaryContentRepository.findById(user.getProfileImageId())
                            : null;

                    return new UserReponseDto(
                            user.getUuid(),
                            user.getNickName(),
                            user.getEmail(),
                            user.getNickName(),
                            (profile != null ? profile.getFilePath() : null),
                            (profile != null ? profile.getContentType() : null),
                            (status != null && status.isOnline()),
                            user.getCreateAt()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(UUID uuid, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(uuid);
        if (user == null) {
            System.out.println("존재하지 않는 유저입니다.");
        }

        // 닉네임 변경
        else if (userUpdateRequestDto.getNickName() != null
                && !userUpdateRequestDto.getNickName().isBlank()) {
            user.setNickName(userUpdateRequestDto.getNickName());
        }

        // 비밀번호 변경
        else if (userUpdateRequestDto.getNewPassword() != null && !userUpdateRequestDto.getNewPassword().isBlank()) {
            user.setUserPassword(userUpdateRequestDto.getNewPassword());
        }

        // 프로필 이미지 교체
        else if (userUpdateRequestDto.getProfileImagePath() != null && !userUpdateRequestDto.getProfileImagePath().isBlank()) {
            BinaryContent newProfile = new BinaryContent(
                    user.getUuid(),
                    null,
                    userUpdateRequestDto.getProfileImagePath(),
                    "image/png"
            );
            binaryContentRepository.save(newProfile);

            // 기존 프로필 삭제
            if (user.getProfileImageId() != null) {
                try {
                    binaryContentRepository.delete(user.getProfileImageId());
                } catch (Exception e) {
                    System.out.println("기존 프로필 삭제 중 문제 발생 : " + e.getMessage());
                }
            }
            user.setProfileImage(newProfile.getUuid());
        }

        userRepository.save(user);
        System.out.println("[User 수정 완료] : " + user.getUserId());
    }

    @Override
    public void deleteUser(UUID uuid) {

    }


    //작동 테스트
//    public User[] runUserService() {
//
//        // 유저 전체 조회
//        userList();
//
//        // 유저 닉네임 수정 Bob->Minsu
//        updateUser(users[1].getUuid(), "Minsu");
//
//        // 유저 password 수정 Bob : 0000pass -> 012456pw
//        updatePassword(users[1].getUuid(), "012456pw");
//
//        // 유저 단건 조회
//        System.out.println("[유저 검색] : " + readUser(users[1].getUuid()));
//
//        // 유저 삭제
//        deleteUser(users[3].getUuid());
//        System.out.println("탈퇴 : " + users[3].getNickName() + "님");
//
//        // 전체 조회
//        userList();
//
//        return users;
//    }

    //유저 전체 조회
    public void userList() {
        System.out.println("[유저 전체 조회]");
        Set<String> userSet = new HashSet<>();
        for (UserReponseDto u : readAllUser()) {
            if (userSet.add(u.getUsername())) { // userId 기준
                System.out.println("ID: " + u.getUsername() + " / Name: " + u.getNickname());
            }
        }
    }
}
