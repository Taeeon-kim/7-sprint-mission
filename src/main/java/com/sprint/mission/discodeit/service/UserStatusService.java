package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusService {

    UUID createUserStatus(UserStatusRequestDto userId);

    UserStatus getUserStatusById(UUID id);

}
