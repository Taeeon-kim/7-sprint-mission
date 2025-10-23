package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class User extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private String email;
    private String password;
    private RoleType role; // NOTE: 채널 맴버의 타입이아닌 회원의 역할을 말하는것(일반유저, 어드민)
    private String phoneNumber;
//    private final List<User> friends;


    private User() {
    }

    public User(String nickname, String email, String password, RoleType role, String phoneNumber) {
        this();
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
        this.phoneNumber = phoneNumber;
    }

    public User(User other) { //  NOTE: 복사용, 복사 생성자

        super(other);
        this.nickname = other.nickname;
        this.email = other.email;
        this.password = other.password;
        this.role = other.role;
        this.phoneNumber = other.phoneNumber;
//        this.friends = new ArrayList<>(other.friends);
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

    public boolean updatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isBlank() && !phoneNumber.equals(this.phoneNumber)) {
            this.phoneNumber = phoneNumber;
            return true;
        }
        return false;
    }




    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ",nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", phoneNumber='" + phoneNumber +
                '}';
    }
}
