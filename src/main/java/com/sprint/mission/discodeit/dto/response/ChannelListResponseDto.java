package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
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
    private User userId;
    private List<ChannelResponseDto> channelList;

}
