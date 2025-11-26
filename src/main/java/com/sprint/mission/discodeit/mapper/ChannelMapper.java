package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;

    public ChannelResponseDto toDto(Channel channel){
        List<User> participants = readStatusRepository.findByChannelId(channel.getId()).stream()
                .map(ReadStatus::getUser)
                .collect(Collectors.toList());

        Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId(), PageRequest.of(0, 1))
                .stream()
                .map(Message::getCreatedAt)
                .findFirst()
                .orElse(null);

        List<UserResponseDto> userResponseDtoList = participants.stream()
                .map(userMapper::toDto)
                .toList();
        return new ChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                userResponseDtoList,
                channel.getType(),
                lastMessageAt

        );
    }
}
