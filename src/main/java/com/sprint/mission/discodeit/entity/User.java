package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    private String userId; //가입 Id
    private String password; //비밀번호
    private String email; //이메일
    private String userName; //유저 이름
    private UUID profileImageId; //프로필

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
