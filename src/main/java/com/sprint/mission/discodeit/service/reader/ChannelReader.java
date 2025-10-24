package com.sprint.mission.discodeit.service.reader;

import com.sprint.mission.discodeit.entity.Channel;
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
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));
    }
}
