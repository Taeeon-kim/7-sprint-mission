package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelListResponseDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto createPublicChannel(ChannelCreateRequestDto channelCreateRequestDto); //채널 생성
    ChannelResponseDto createPrivateChannel(ChannelCreateRequestDto channelCreateRequestDto); //채널 생성

    ChannelResponseDto findChannel(UUID channelId); //채널 내용 보기

    ChannelListResponseDto findAllByUserId(UUID userId);

    void updateChannel(ChannelUpdateRequestDto channelUpdateRequestDto); //채널 수정

    void deleteChannel(UUID channelId); //채널 삭제


}
