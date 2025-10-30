package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelCreateRequestDto {

    private ChannelType channelType;
    private String channelName; // PUBLIC 일때 사용
    private String channelDescription; // 현재 엔티티에 없으면 무시 가능
    private List<UUID> participantIds; // PRIVATE 일 때 사용

}
