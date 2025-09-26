package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class User extends BasicEntity {
    private String nickname;
    private String email;
    private String password;
    private RoleType role; // NOTE: 채널 맴버의 타입이아닌 회원의 역할을 말하는것(일반유저, 어드민)
    private String phoneNumber;
    private List<User> friends;


    public User() {
        friends = new ArrayList<>();
    }

    public User(String nickname, String email, String password, RoleType role, String phoneNumber) {
        this();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }
    public User(User other){
        super(other.getId());
        this.nickname = other.nickname;
        this.email = other.email;
        this.password = other.password;
        this.role = other.role;
        this.phoneNumber = other.phoneNumber;
        this.friends = other.friends;
    }



    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public RoleType getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void updateNickname(String nickname) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }

    }

    public void updatePassword(String password) {
        if (password != null && !password.isBlank()) {
            this.password = password;
        }
    }

    public void updateEmail(String email) {
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
    }

    public void updatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            this.phoneNumber = phoneNumber;
        }
    }


    public List<User> getFriends() {
        return friends;
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", friends=" + friends +
                '}';
    }
}
