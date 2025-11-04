package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelListResponseDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;

import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto createPublicChannel(ChannelPublicCreateRequestDto channelCreateRequestDto); //채널 생성
    ChannelResponseDto createPrivateChannel(ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto); //채널 생성

    ChannelResponseDto findById(UUID channelId); //채널 내용 보기

    ChannelListResponseDto findAllChannel(UUID userId);

    void updateChannel(UUID uuid, ChannelUpdateRequestDto channelUpdateRequestDto); //채널 수정

    void deleteChannel(UUID channelId); //채널 삭제


}
