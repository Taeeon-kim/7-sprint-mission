package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
    private String userId;
    private String email;
    private String password;
    private String nickName;
    private String profileImagePath; // 선택적 이미지 등록
}
