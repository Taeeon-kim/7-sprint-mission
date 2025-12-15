package com.sprint.mission.discodeit.service.reader;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class ChannelReader {

    private final ChannelRepository channelRepository;

    public ChannelReader(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public Channel findChannelOrThrow(UUID channelId) {
        if (channelId == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));
    }
}
