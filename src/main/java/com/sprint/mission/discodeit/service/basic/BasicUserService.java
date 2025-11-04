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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    // 레파지토리 의존성 주입
    private final UserRepository userRepository;

    @Override
    public void createUser(UserCreateRequestDto userCreateRequestDto) {

    }

    @Override
    public UserReponseDto findById(UUID uuid) {
        return null;
    }

    @Override
    public List<UserReponseDto> findAllUser() {
        return List.of();
    }

    @Override
    public void updateUser(UUID uuid, UserUpdateRequestDto userUpdateRequestDto) {

    }

    @Override
    public void deleteUser(UUID uuid) {

    }

    //    // 레파지토리 의존성 주입
//    private final BinaryContentRepository binaryContentRepository;
//    private final UserStatusRepository userStatusRepository;
//
//    //CRUD
//    @Override
//    public void createUser(UserCreateRequestDto userCreateRequestDto) {
//        //중복검사
//        boolean exists = userRepository.findAll().stream()
//                .anyMatch(u -> u.getUserId().equals(userCreateRequestDto.getUserId())
//                        || u.getEmail().equals(userCreateRequestDto.getEmail())
//                        || u.getNickName().equals(userCreateRequestDto.getNickName()));
//        if (exists) {
//            throw new IllegalStateException("이미 존재하는 Id 또는 nickname 또는 email 입니다.");
////            System.out.println("이미 존재하는 Id 또는 nickname 또는 email 입니다.");
//        }
//
//        // user생성
//        User user = new User(
//                userCreateRequestDto.getUserId(),
//                userCreateRequestDto.getEmail(),
//                userCreateRequestDto.getPassword(),
//                userCreateRequestDto.getNickName(),
//                null,
//                null
//        );
//        userRepository.save(user);
//
//        // 유저 상태 생성
//        UserStatus status = new UserStatus(user.getUuid());
//        userStatusRepository.save(status);
//
//        //프로필 이미지 등록(선택)
//        if (userCreateRequestDto.getProfileImagePath() != null
//                && !userCreateRequestDto.getProfileImagePath().isBlank()) {
//            BinaryContent profile = new BinaryContent(
//                    user.getUuid(),
//                    null,
//                    userCreateRequestDto.getProfileImagePath(),
//                    "image/png"
//            );
//            binaryContentRepository.save(profile);
//
//            // user의 profileImageId 필드에 BinaryContent.uuid 연결
//            user.setProfileImage(profile.getUuid());
//            userRepository.save(user); // 업데이트 저장
//        }
//        System.out.println("[User 생성 완료] : " + user.getUserId());
//    }
//
//    @Override
//    public UserReponseDto readUser(UUID uuid) {
//        User user = userRepository.findById(uuid);
//        if (user == null) return null;
//
//        UserStatus status = userStatusRepository.findByUserId(uuid);
//        BinaryContent profile = (user.getProfileImageId() != null)
//                ? binaryContentRepository.findById(user.getProfileImageId())
//                : null;
//        return new UserReponseDto(
//                user.getUuid(),
//                user.getUserId(),
//                user.getEmail(),
//                user.getNickName(),
//                (profile != null ? profile.getFilePath() : null),
//                (profile != null ? profile.getContentType() : null),
//                status != null ? status.getStatus() : null,
//                user.getCreateAt()
//        );
//    }
//
//    @Override
//    public List<UserReponseDto> readAllUser() {
//        return userRepository.findAll().stream()
//                .map(user -> {
//                    UserStatus status = userStatusRepository.findByUserId(user.getUuid());
//                    BinaryContent profile = (user.getProfileImageId() != null)
//                            ? binaryContentRepository.findById(user.getProfileImageId())
//                            : null;
//
//                    return new UserReponseDto(
//                            user.getUuid(),
//                            user.getUserId(),
//                            user.getEmail(),
//                            user.getNickName(),
//                            (profile != null ? profile.getFilePath() : null),
//                            (profile != null ? profile.getContentType() : null),
//                            status != null ? status.getStatus() : null,
//                            user.getCreateAt()
//                    );
//                })
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void updateUser(UUID uuid, UserUpdateRequestDto userUpdateRequestDto) {
//        User user = userRepository.findById(uuid);
//        if (user == null) {
//            System.out.println("존재하지 않는 유저입니다.");
//        }
//
//        // 닉네임 변경
//        if (userUpdateRequestDto.getNickName() != null
//                && !userUpdateRequestDto.getNickName().isBlank()) {
//            user.setNickName(userUpdateRequestDto.getNickName());
//        }
//
//        // 비밀번호 변경
//        if (userUpdateRequestDto.getNewPassword() != null && !userUpdateRequestDto.getNewPassword().isBlank()) {
//            user.setUserPassword(userUpdateRequestDto.getNewPassword());
//        }
//
//        // 프로필 이미지 교체
//        if (userUpdateRequestDto.getProfileImagePath() != null && !userUpdateRequestDto.getProfileImagePath().isBlank()) {
//            UUID existingProfileId = user.getProfileImageId(); // 기존 프로필 UUID
//            BinaryContent newProfile = new BinaryContent(
//                    user.getUuid(),
//                    null,
//                    userUpdateRequestDto.getProfileImagePath(),
//                    "image/png"
//            );
//            binaryContentRepository.save(newProfile);
//
//            // user의 profileImageId 업데이트
//            user.setProfileImage(newProfile.getUuid());
//
//            // 기존 프로필 삭제
//            if (existingProfileId != null) {
//                try {
//                    binaryContentRepository.delete(user.getProfileImageId());
//                } catch (Exception e) {
//                    System.out.println("기존 프로필 삭제 중 문제 발생 : " + e.getMessage());
//                }
//            }
//            user.setProfileImage(newProfile.getUuid());
//        }
//
//        userRepository.save(user);
//        System.out.println("[User 수정 완료] : " + user.getUserId());
//    }
//
//    @Override
//    public void deleteUser(UUID uuid) {
//        User user = userRepository.findById(uuid);
//        if (user == null) throw new IllegalArgumentException("삭제하려는 유저가 존재하지 않습니다.");
//
//        // 상태 삭제
//        UserStatus status = userStatusRepository.findByUserId(uuid);
//        if( status != null){
//            userStatusRepository.delete(uuid);
//        }
//
//        //프로필 삭제
//        if (user.getProfileImageId() != null) {
//            binaryContentRepository.delete(user.getProfileImageId());
//        }
//
//        // 유저 삭제
//        userRepository.delete(uuid);
//        System.out.println("[User 삭제 완료] UserID : " + user.getUserId());
//    }
//
//    public List<User> getReadUsers(){
//        return userRepository.findAll();
//    }
//
//    public void runTest() {
//        // User 등록
//        UserCreateRequestDto[] userCreateRequestDtos = new UserCreateRequestDto[]{
//                new UserCreateRequestDto("test00", "alice123@gmail.com", "pass123", "Alice", null, null),
//                new UserCreateRequestDto("test02", "name000@gmail.com", "0000pass", "Bob", null, null),
//                new UserCreateRequestDto("test03", "chilysource@gmail.com", "12341234", "Chily", null, null),
//                new UserCreateRequestDto("test05", "tomtom00@gmail.com", "pw123456", "Tom", null, null)
//        };
//
//        //유저 생성
//        for (UserCreateRequestDto userCreateRequestDto : userCreateRequestDtos) {
//            createUser(userCreateRequestDto);
//        }
//
//        // 전체 조회
//        userList();
//
//        // 유저 수정
//        User userUpdate = userRepository.findAll().stream()
//                .filter(u -> u.getUserId().equals("test02"))
//                .findFirst().orElseThrow(() -> new RuntimeException("수정할 유저를 찾을 수 없습니다."));
//        updateUser(userUpdate.getUuid(), new UserUpdateRequestDto("Minsu", "0123456pw", "고양이.png"));
//
//        // 수정 후 단건 조회
//        UserReponseDto userReponseDto = readUser(userUpdate.getUuid());
//        System.out.println("수정된 유저 : " + userReponseDto.getUserid()
//                + " / Name : " + userReponseDto.getNickname()
//                + " / Status : " + userReponseDto.getStatus()
//                + " / profile : " + userReponseDto.getProfileImagePath());
//        userList();
//
//        // 유저 삭제
//        User deleteUser = userRepository.findAll().stream()
//                .filter(u->u.getUserId().equals("test02"))
//                .findFirst().orElseThrow();
//        deleteUser(deleteUser.getUuid());
//
//        // 전체 조회
//        userList();
//    }
//
//    //유저 전체 조회
//    public void userList() {
//        System.out.println("[유저 전체 조회]");
//        Set<String> userSet = new HashSet<>();
//        for (UserReponseDto u : readAllUser()) {
//            if (userSet.add(u.getUserid())) { // userId 기준
//                System.out.println("ID: " + u.getUserid() + " | Name: " + u.getNickname()
//                        + " | Status : " + u.getStatus() + "| Profile :" + u.getProfileImagePath());
//            }
//        }
//    }
}
