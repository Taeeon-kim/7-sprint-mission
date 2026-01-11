package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, BinaryContentMapper.class}
)
public interface MessageMapper {

    @Mapping(target = "channelId", source = "channel.id")
    MessageResponseDto toDto(Message message);

    @Mapping(target = "channelId", source = "channel.id")
    MessageUpdateResponseDto toUpdateDto(Message message);
}
