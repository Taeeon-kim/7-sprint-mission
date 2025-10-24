package com.sprint.mission.discodeit.config;


import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
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
    public JCFBinaryContentRepository jcfBinaryContentRepository(InMemoryStore store) {
        return new JCFBinaryContentRepository(store.binaryContents);
    }
}
