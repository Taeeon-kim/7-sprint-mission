package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    UUID createReadStatus(ReadStatusCreateRequestDto requestDto);

    ReadStatusResponseDto getReadStatus(UUID readStatusId);

    List<ReadStatusResponseDto> getAllReadStatusesByUserId(UUID userId);

    void updateReadStatus(UUID id, ReadStatusUpdateRequestDto requestDto);

    void deleteReadStatus(UUID id);

}
