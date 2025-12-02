package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Schema(description = "Private Channel 생성 정보")
public class ChannelPrivateCreateRequestDto {

//    private ChannelType channelType;
    private List<UUID> participantIds; // PRIVATE 일 때 사용
//    private String description;

}
