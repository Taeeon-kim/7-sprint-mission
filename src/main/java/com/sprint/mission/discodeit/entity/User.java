package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 사용을 위한 기본 생성자 추가
public class User extends BaseUpdatableEntity {

//    private String userId; //가입 Id
    @Column(length = 60, nullable = false)
    private String password; //비밀번호

    @Column(length = 100, nullable = false, unique = true)
    private String email; //이메일

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String userName; //유저 이름

//    private UUID profileImageId; //프로필
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", unique = true)
    private BinaryContent profile;

    // 1:1 관계
    @Setter(AccessLevel.PROTECTED)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;

    // N:M 관계
    private List<Channel> channels;

    // N:M 관계
    private List<ReadStatus> readStatuses;

    public User(String password, String email, String userName, BinaryContent profile) {
        super();
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.profile = profile;
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

    public void setProfileImageId(BinaryContent newProfile) {
        if(newProfile !=null&&!newProfile.equals(this.profile)) { //프로필 변경
            this.profile = newProfile;
            setUpdatedAt(Instant.now());
        }
    }
}
