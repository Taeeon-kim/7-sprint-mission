package com.sprint.mission.discodeit.integration.fixtures;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

public final class ReadStatusFixture {

    private ReadStatusFixture() { }

    public static ReadStatus joinChannel(User user, Channel channel, ReadStatusRepository readStatusRepository) {
        return readStatusRepository.save(new ReadStatus(user, channel));
    }
}
