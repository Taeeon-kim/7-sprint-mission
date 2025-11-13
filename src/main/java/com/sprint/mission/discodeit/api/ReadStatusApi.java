package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateCommand;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface ReadStatusApi {

    ResponseEntity<ReadStatusResponseDto> getReadStatus(@PathVariable UUID readStatusId);

    ResponseEntity<UUID> createReadStatus(ReadStatusCreateRequestDto request);

    ResponseEntity<Void> updateReadStatus(UUID id, ReadStatusUpdateRequestDto requestDto);

    ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatusesByUserId(UUID userId);
}
