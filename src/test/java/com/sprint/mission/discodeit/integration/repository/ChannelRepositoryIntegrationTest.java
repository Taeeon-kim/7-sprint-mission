package com.sprint.mission.discodeit.integration.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.integration.fixtures.UserFixture;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest // 내부 @Transactional 있어서 자동 롤백
//@AutoConfigureTestDatabase(replace = Replace.NONE) // DataJpaTest쓸때 자동으로 임베디드 DB를 검색하는데 testImplementation 에 설정(h2)한걸로 안하고 runtimeOnly 실행하고자 넣어야됨
public class ChannelRepositoryIntegrationTest {

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @Test
    void channelTest(){

        User user = UserFixture.createUserWithStatus(userRepository);

        List<Channel> allVisibleByUserId = channelRepository.findAllVisibleByUserId(user.getId());
        System.out.println("allVisibleByUserId = " + allVisibleByUserId);

        allVisibleByUserId.forEach(channel -> System.out.println(channel.getType()));

    }


}
