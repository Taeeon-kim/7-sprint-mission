package com.sprint.mission.discodeit.slice.jpa;

import com.sprint.mission.discodeit.config.JpaAuditingConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("ChannelRepository JPA Slice Test")
class ChannelRepositorySliceTest {

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReadStatusRepository readStatusRepository;

    @Nested
    @DisplayName("findAllVisibleByUserId")
    class FindAllVisibleByUserId {

        @Test
        @DisplayName("[Success] PUBLIC 채널은 항상 조회된다")
        void shouldReturnPublicChannels() {
            // given
            User user = userRepository.save(User.create("user", "u@u.com", "pw", null));

            Channel publicChannel =
                    channelRepository.save(Channel.createPublicChannel("public", "desc"));

            // when
            List<Channel> result =
                    channelRepository.findAllVisibleByUserId(user.getId());

            // then
            assertEquals(1, result.size());
            assertEquals(ChannelType.PUBLIC, result.get(0).getType());
        }

        @Test
        @DisplayName("[Success] PRIVATE 채널이라도 ReadStatus 있으면 조회된다")
        void shouldReturnPrivateChannel_whenReadStatusExists() {
            // given
            User user = userRepository.save(User.create("user", "u@u.com", "pw", null));

            Channel privateChannel =
                    channelRepository.save(Channel.createPrivateChannel());

            readStatusRepository.save(
                    new ReadStatus(user, privateChannel)
            );

            // when
            List<Channel> result =
                    channelRepository.findAllVisibleByUserId(user.getId());

            // then
            assertEquals(1, result.size());
            assertEquals(ChannelType.PRIVATE, result.get(0).getType());
        }

        @Test
        @DisplayName("[Fail] PRIVATE 채널 + ReadStatus 없으면 조회되지 않는다")
        void shouldNotReturnPrivateChannel_whenNoReadStatus() {
            // given
            User user = userRepository.save(User.create("user", "u@u.com", "pw", null));

            channelRepository.save(Channel.createPrivateChannel());

            // when
            List<Channel> result =
                    channelRepository.findAllVisibleByUserId(user.getId());

            // then
            assertTrue(result.isEmpty());
        }
    }
}