package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;

import java.util.UUID;

public interface ReadStatusService {

    UUID createReadStatus(ReadStatusCreateRequestDto requestDto);

}
