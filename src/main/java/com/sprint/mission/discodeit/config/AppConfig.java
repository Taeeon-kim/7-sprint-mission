package com.sprint.mission.discodeit.config;


import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.store.InMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

    @Bean
    public InMemoryStore inMemoryStore() {
        return new InMemoryStore();
    }

    @Bean
    public BinaryContentRepository jcfBinaryContentRepository(InMemoryStore store) {
        return new JCFBinaryContentRepository(store.binaryContents);
    }

    @Bean
    public UserStatusRepository jcfUserStatusRepository(InMemoryStore store) {
        return new JCFUserStatusRepository(store.userStatusses);
    }
}
