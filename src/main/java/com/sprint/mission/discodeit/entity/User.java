package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserUpdateParams;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.type.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class User extends BaseUpdatableEntity {
    private String nickname;
    private String email;
    private String password;
    private RoleType role; // NOTE: 채널 맴버의 타입이아닌 회원의 역할을 말하는것(일반유저, 어드민)
    private UUID profileId;

    @Builder
    private User(String nickname, String email, String password, RoleType role, UUID profileId) {
        super();
          /*
        엔티티의 업데이트 메서드(전이)는 항상 관련 불변식을 재확인하는 검증을 포함한다.
        이 검증이 “입력 가드처럼 보일 수” 있지만, 목적은 외부 입력 차단이 아니라 내 상태 보전이기 때문에 불변식 검사라고 부른다.
         */

        if (nickname == null || nickname.isBlank()) throw new IllegalArgumentException("nickname invalid");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("email invalid");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password invalid");
        if (role == null) throw new IllegalArgumentException("role invalid");
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.profileId = profileId;
    }

    public static User create(String nickname, String email, String password, RoleType role, UUID profileId) {
        return new User(nickname, email, password, role, profileId);
    }

    public boolean update(UserUpdateParams request) {
        boolean changeFlag = false;
        changeFlag |= this.updateNickname(request.nickname());
        changeFlag |= this.updateEmail(request.email());
        changeFlag |= this.updatePassword(request.password());
        changeFlag |= this.updateProfileId(request.profileId());
        if (changeFlag) {
            this.setUpdatedAt(Instant.now());
        }
        return changeFlag;
    }

    private boolean updateProfileId(UUID profileId) {
        if (profileId != null && !profileId.equals(this.profileId)) {
            this.profileId = profileId;
            return true;
        }
        return false;
    }

    public boolean updateNickname(String nickname) {
        if (nickname != null && !nickname.isBlank() && !nickname.equals(this.nickname)) {
            this.nickname = nickname;
            return true;
        }
        return false;

    }

    public boolean updatePassword(String password) {
        if (password != null && !password.isBlank() && !password.equals(this.password)) {
            this.password = password;
            return true;
        }
        return false;
    }

    public boolean updateEmail(String email) {
        if (email != null && !email.isBlank() && !email.equals(this.email)) {
            this.email = email;
            return true;
        }
        return false;
    }

}
