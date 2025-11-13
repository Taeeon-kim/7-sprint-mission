package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;

public interface ChannelApi {

    ResponseEntity<List<ChannelResponseDto>> getAllChannels(UUID userId);

    ResponseEntity<ChannelResponseDto> createChannelPublic(ChannelCreateRequestDto request);

    ResponseEntity<ChannelResponseDto> createChannelPrivate(ChannelCreateRequestDto request);

    ResponseEntity<ChannelResponseDto> getChannel(UUID channelId);

    ResponseEntity<Void> updateChannel(UUID channelId, ChannelUpdateRequestDto request);

    ResponseEntity<Void> deleteChannel(UUID channelId);
}
