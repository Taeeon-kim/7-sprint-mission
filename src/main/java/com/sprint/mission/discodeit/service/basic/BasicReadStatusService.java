package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;

    @Override
    public UUID createReadStatus(ReadStatusCreateRequestDto requestDto) {
        ReadStatus readStatus = new ReadStatus(requestDto.userId(), requestDto.channelId(), Instant.now());
        ReadStatus saved = readStatusRepository.save(readStatus);
        return saved.getId();
    }
}
