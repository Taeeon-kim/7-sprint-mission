package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Public Channel 생성 정보")
public class ChannelPublicCreateRequestDto {

    private ChannelType channelType;
//    private String channelName; // PUBLIC 일때 사용
    private String name;
}
