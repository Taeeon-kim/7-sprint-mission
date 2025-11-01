package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelPrivateCreateRequestDto {

    private ChannelType channelType;
    private List<UUID> participantIds; // PRIVATE 일 때 사용

}
