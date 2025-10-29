package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @ToString
//@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    //명시적으로 선언하는 게 좋대서. 직렬화, 역직렬화 시 클래스 버전 의미
    private final String userId;  //아이디 string
    private final String email; // 이메일
    private String userPassword;    //비밀번호 string 수정가능
    private String nickName;    //닉네임 = 사용자명 string, 수정가능
    private UUID profileImageId; // 변경 가능
    private UserStatus userStatus;

//    public User(String userId, String email, String userPassword, String nickName, UUID profileImageId, StatusType userStatus) {
//        super();
//        this.userId = userId;
//        this.email = email;
//        this.userPassword = userPassword;
//        this.nickName = nickName;
//        this.profileImageId = profileImageId;
//        this.userStatus = userStatus;
//    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        setUpdatedAt(Instant.now()); //변경 시 시간 갱신
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        setUpdatedAt(Instant.now()); //변경 시 시간 갱신
    }

    // 프로필 이미지 교체
    public void setProfileImage(UUID profileImageId) {
        this.profileImageId = profileImageId;
        setUpdatedAt(Instant.now());
    }
}
