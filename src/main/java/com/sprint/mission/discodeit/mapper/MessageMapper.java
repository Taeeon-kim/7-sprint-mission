package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final UserMapper userMapper;

    private final BinaryContentMapper binaryContentMapper;

    public MessageResponseDto toDto(Message message) {
        UserResponseDto userResponseDto = Optional.ofNullable(message.getAuthor())
                .map(userMapper::toDto)
                .orElse(null);
        List<BinaryContentResponseDto> binaryContentResponseDtoList = message.getAttachments()
                .stream()
                .map(binaryContentMapper::toDto)
                .toList();

        return new MessageResponseDto(
                message.getId(),
                message.getChannel().getId(),
                userResponseDto,
                message.getContent(),
                binaryContentResponseDtoList,
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }

    public MessageUpdateResponseDto toUpdateDto(Message message) {
        UserResponseDto userResponseDto = Optional.ofNullable(message.getAuthor())
                .map(userMapper::toDto)
                .orElse(null);
        List<BinaryContentResponseDto> binaryContentResponseDtoList = message.getAttachments()
                .stream()
                .map(binaryContentMapper::toDto)
                .toList();

        return new MessageUpdateResponseDto(
                message.getId(),
                message.getChannel().getId(),
                userResponseDto,
                message.getContent(),
                binaryContentResponseDtoList,
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }

}
