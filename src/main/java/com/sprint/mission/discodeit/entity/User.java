package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

    private String userId; //가입 Id
    private String password; //비밀번호
    private String email; //이메일
    private String userName; //유저 이름
    private UUID profileImageId; //프로필

    // 1:1 관계
    private UserStatus userStatus;

    // N:M 관계
    private List<Channel> channels;

    // N:M 관계
    private List<ReadStatus> readStatuses;

    public User(String userId, String password, String email, String userName, UUID profileImageId) {
        super();
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.profileImageId = profileImageId;
    }

    public void setPassword(String newPassword) {
        if (newPassword != null && !newPassword.equals(this.password)) { //비밀번호 변경
            this.password = newPassword;
            setUpdatedAt(Instant.now());
        }
    }

    public void setEmail(String newEmail) {
        if (newEmail != null && !newEmail.equals(this.email)) { //이메일 변경
            this.email = newEmail;
            setUpdatedAt(Instant.now());
        }
    }

    public void setUserName(String newUserName) {
        if(newUserName !=null&&!newUserName.equals(this.userName)) {
            this.userName = newUserName;
            setUpdatedAt(Instant.now());
        }
    }

    public void setProfileImageId(UUID newProfileImageId) {
        if(newProfileImageId !=null&&!newProfileImageId.equals(this.profileImageId)) { //프로필 변경
            this.profileImageId = newProfileImageId;
            setUpdatedAt(Instant.now());
        }
    }
}
