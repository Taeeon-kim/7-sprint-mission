package com.sprint.mission.discodeit.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateRequestDto {
    private String nickname;
    private String newPassword;
    private String profileImagePath; //선택적으로 이미지 대체
}
