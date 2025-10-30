package com.sprint.mission.discodeit.config;


import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.store.InMemoryStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    @Bean
    public ReadStatusRepository jcfReadStatusRepository(InMemoryStore store) {
        return new JCFReadStatusRepository(store.readStatuses);
    }

//    @Bean
//    // 이 메서드의 리턴 객체는 조건부로 빈 등록이 된다.
//    @ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
//    public UserRepository jcfUserRepository(InMemoryStore store) {
//            return new JCFUserRepository(store.users);
//    }

//    @Bean
//    // 이 메서드의 리턴 객체는 조건부로 빈 등록이 된다.
//    @ConditionalOnProperty(prefix = "discodeit.repository",  // 접두어
//            name = "type", // 참조해야할 속성 키값
//            havingValue = "file") //
//    public UserRepository fileUserRepository() {
//        return new FileUserRepository();
//    }
}
