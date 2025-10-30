package com.sprint.mission.discodeit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelListResponseDto {

    // 특정 유저 기준 반환할 채널 목록
    private List<ChannelListResponseDto> channelList;

}
