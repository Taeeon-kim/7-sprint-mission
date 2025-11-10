package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResponseDto create(UserStatusCreateRequestDto userStatusCreateRequestDto);

    UserStatusResponseDto findById(UUID uuid);

    List<UserStatusResponseDto> findAll();

    UserStatusResponseDto update(UUID uuid, UserStatusUpdateRequestDto userStatusUpdateRequestDto);

    UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateRequestDto userStatusUpdateRequestDto);

    void delete(UUID uuid);
}
