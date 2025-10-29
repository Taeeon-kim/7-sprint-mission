package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UUID createUserStatus(UserStatusRequestDto userId);

    UserStatusResponseDto getUserStatusById(UUID id);

    List<UserStatusResponseDto> getAllUserStatuses();

    void updateUserStatus(UUID id, UserStatusUpdateDto userStatusUpdateDto);

    void updateUserStatusByUserId(UUID userId, UserStatusUpdateDto userStatusUpdateDto);

}
