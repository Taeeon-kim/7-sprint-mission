package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String userName;
    private String email;
    private String newPassword;
    private MultipartFile profileImageㅖ; //선택적으로 이미지 대체
}
