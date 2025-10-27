package com.sprint.mission.discodeit.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserCreateRequestDto {
    private String userName;
    private String email;
    private String nickName;
    private String password;
    private String nickname;
    private String profileImagePath; // 선택적 이미지 등록
}
