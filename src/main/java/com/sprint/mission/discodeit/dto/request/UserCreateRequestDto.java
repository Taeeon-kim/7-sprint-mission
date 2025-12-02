package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.StatusType;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Parameter;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
    private String userId;
    private String email;
    private String password;
    private String userName;
    @Schema(description = "User 프로필 이미지")
    private MultipartFile profile;//profileImagePath; // 선택적 이미지 등록
//    private String status; //유저 접속 상태
}
