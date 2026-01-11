package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.*;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusResponseDto createReadStatus(ReadStatusCreateRequestDto requestDto);

    ReadStatusResponseDto getReadStatus(UUID readStatusId);

    List<ReadStatusResponseDto> getAllReadStatusesByUserId(UUID userId);

    ReadStatusUpdateResponseDto updateReadStatus(ReadStatusUpdateCommand command);

    void deleteReadStatus(UUID id);

}
