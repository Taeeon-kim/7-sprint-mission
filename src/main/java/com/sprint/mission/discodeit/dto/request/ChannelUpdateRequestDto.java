package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * 채널 수정 요청 DTO, PRIAVTE는 서비스에서 예외 처리(수정 불가)
 */
@Getter
@Setter
@AllArgsConstructor
public class ChannelUpdateRequestDto {

    private UUID channelId;
//    private String newChannelName;
    private String newName;
}
