package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String nickName;
    private String newPassword;
    private String profileImagePath; //선택적으로 이미지 대체
}
