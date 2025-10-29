package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UUID createUserStatus(UserStatusRequestDto userId);

    UserStatusResponseDto getUserStatus(UUID id);

    List<UserStatusResponseDto> getAllUserStatuses();

    void updateUserStatus(UUID id, UserStatusUpdateRequestDto userStatusUpdateRequestDto);

    void updateUserStatusByUserId(UUID userId, UserStatusUpdateRequestDto userStatusUpdateRequestDto);

    void deleteUserStatus(UUID id);

}
