package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;
    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusResponseDto createReadStatus(ReadStatusCreateRequestDto requestDto) {
        if (requestDto.userId() == null || requestDto.channelId() == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        User user = userReader.findUserOrThrow(requestDto.userId());
        Channel channel = channelReader.findChannelOrThrow(requestDto.channelId());


        if (readStatusRepository.existsByUserIdAndChannelId(requestDto.userId(), requestDto.channelId())) {
            throw new IllegalArgumentException("이미 존재합니다.");
        }


        ReadStatus readStatus = ReadStatus.builder()
                .user(user)
                .channel(channel)
                .build();

        ReadStatus saved = readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadStatusResponseDto getReadStatus(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(() -> new NoSuchElementException("읽음 상태가 존재하지 않습니다."));
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusResponseDto> getAllReadStatusesByUserId(UUID userId) {
        List<ReadStatus> allByUserId = readStatusRepository.findAllByUserId(userId);
        return allByUserId.stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ReadStatusUpdateResponseDto updateReadStatus(ReadStatusUpdateCommand command) {
        ReadStatus readStatusById = readStatusRepository.findById(command.id()).orElseThrow();

        boolean isUpdated = readStatusById.updateReadAt(command.readAt());
        if (isUpdated) {
            ReadStatus saved = readStatusRepository.save(readStatusById);
            return ReadStatusUpdateResponseDto.from(saved);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }

}
