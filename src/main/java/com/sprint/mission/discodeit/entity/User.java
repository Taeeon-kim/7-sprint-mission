package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User extends BaseEntity {

    //명시적으로 선언하는 게 좋대서. 직렬화, 역직렬화 시 클래스 버전 의미
    private static final long serialVersionUID = 1L;
    private final String userId;  //아이디 string
    private String userPassword;    //비밀번호 string 수정가능
    private String nickName;    //닉네임 = 사용자명 = 별명 string, 수정가능

    public User(String userId, String userPassword, String nickName) {
        super();
        this.userId = userId;
        this.userPassword = userPassword;
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        setUpdatedAt(System.currentTimeMillis()); //변경 시 시간 갱신
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        setUpdatedAt(System.currentTimeMillis()); //변경 시 시간 갱신
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
