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

    public void setUpdate(String newPassword, String newEmail, String newUserName, UUID newProfileImageId) {
        boolean anyUpdated = false;
        if (newPassword != null && !newPassword.equals(this.password)) { //비밀번호 변경
            this.password = newPassword;
            anyUpdated = true;
        }
        if(newEmail != null && !newEmail.equals(this.email)){ //이메일 변경
            this.email = newEmail;
            anyUpdated = true;
        }
        if(newProfileImageId != null && !newProfileImageId.equals(this.profileImageId)){ //프로필 변경
            this.profileImageId = newProfileImageId;
            anyUpdated = true;
        }

        if(anyUpdated){ //변경 후 시간업데이트
            setUpdatedAt(Instant.now());
        }
    }
}
