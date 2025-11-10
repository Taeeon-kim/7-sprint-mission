package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusDto create(ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        User user = userRepository.findById(readStatusCreateRequestDto.getUserId());
        if (user == null) throw new IllegalArgumentException("User not found");

        Channel channel = channelRepository.findByChannel(readStatusCreateRequestDto.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        ReadStatus existing = readStatusRepository.findByUserAndChannel(
                readStatusCreateRequestDto.getUserId(),
                readStatusCreateRequestDto.getChannelId()
        );
        if (existing != null) throw new IllegalArgumentException("ReadStatus already exists");

        ReadStatus readStatus = new ReadStatus(
                readStatusCreateRequestDto.getUserId(),
                readStatusCreateRequestDto.getChannelId()
        );
        readStatusRepository.save(readStatus);

        return ReadStatusDto.from(readStatus);
    }

    @Override
    public ReadStatusDto findById(UUID uuid) {
        ReadStatus readStatus = readStatusRepository.findById(uuid);
        if (readStatus == null) throw new IllegalArgumentException("ReadStatus not found");
        return ReadStatusDto.from(readStatus);
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID uuid) {
        User user = userRepository.findById(uuid);
        if (user == null) throw new IllegalArgumentException("User not found");
        return readStatusRepository.findByUserId(uuid)
                .stream().map(ReadStatusDto::from).collect(Collectors.toList());
    }

    @Override
    public ReadStatusDto update(ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusUpdateRequestDto.getUuid());
        if(readStatus == null) throw new IllegalArgumentException("ReadStatus not found");
        if (readStatusUpdateRequestDto.getLastReadAt() != null
                && readStatusUpdateRequestDto.getLastReadAt().isAfter(readStatus.getLastActiveAt())) {
            readStatus.setUpdate(readStatusUpdateRequestDto.getLastReadAt());
            readStatusRepository.update(readStatus);
        }
        return ReadStatusDto.from(readStatus);
    }

    @Override
    public void delete(UUID uuid) {
        ReadStatus readStatus = readStatusRepository.findById(uuid);
        if(readStatus == null) throw new IllegalArgumentException("ReadStatus not found");
        readStatusRepository.delete(uuid);
    }
}
