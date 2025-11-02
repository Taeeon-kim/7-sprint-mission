package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;

    @Override
    public UUID createReadStatus(ReadStatusCreateRequestDto requestDto) {
        if (requestDto.userId() == null || requestDto.channelId() == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }


        User user = userReader.findUserOrThrow(requestDto.userId());
        Channel channel = channelReader.findChannelOrThrow(requestDto.channelId());

        for (ReadStatus readStatus : readStatusRepository.findAllByUserId(user.getId())) {
            if (readStatus.getChannelId().equals(channel.getId())) {
                throw new IllegalArgumentException("이미 존재합니다.");
            }
        }


        ReadStatus readStatus = ReadStatus.builder()
                .userId(user.getId())
                .channelId(channel.getId())
                .readAt(Instant.now())
                .build();

        ReadStatus saved = readStatusRepository.save(readStatus);
        return saved.getId();
    }

    @Override
    public ReadStatusResponseDto getReadStatus(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(() -> new NoSuchElementException("읽음 상태가 존재하지 않습니다."));
        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public List<ReadStatusResponseDto> getAllReadStatusesByUserId(UUID userId) {
        List<ReadStatus> allByUserId = readStatusRepository.findAllByUserId(userId);
        return allByUserId.stream()
                .map(ReadStatusResponseDto::from)
                .toList();
    }

    @Override
    public void updateReadStatus(UUID id, ReadStatusUpdateRequestDto requestDto) {
        ReadStatus readStatusById = readStatusRepository.findById(id).orElseThrow();

        boolean isUpdated = readStatusById.updateReadAt(requestDto.readAt());
        if (isUpdated) {
            readStatusRepository.save(readStatusById);
        }
    }

    @Override
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }

}
