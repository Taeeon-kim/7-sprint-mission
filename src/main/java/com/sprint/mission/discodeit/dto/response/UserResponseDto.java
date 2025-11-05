package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String userId;
    private String userName;
    private String userEmail;
    private String userStatus;
    private String profileImagePath;

    public static UserResponseDto from(User user, UserStatus userStatus, BinaryContent binaryContent) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getEmail())
                .userStatus(userStatus != null ? userStatus.getStatus().name() : null)
                .profileImagePath(binaryContent.getFileName())
                .build();
    }
}
