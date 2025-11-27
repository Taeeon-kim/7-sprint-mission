package com.sprint.mission.discodeit.integration.repository;

import com.sprint.mission.discodeit.dto.user.UserUpdateParams;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // 내부 @Transactional 있어서 자동 롤백
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // DataJpaTest쓸때 자동으로 임베디드 DB를 검색하는데 testImplementation 에 설정한걸로 안하고 runtimeOnly 실행하고자 넣어야됨
public class UserRepositoryIntegrationTest {

    @Autowired
    UserRepository userRepository;

    ///  쿼리/유스케이스 관점 테스트 (findByEmail, findByChannel... 등등) TODO: 추가할것



    /// 아래는 인프라 레벨(필드/컬럼 DB & JPA & Auditing 연결잘되는지) 테스트

    @Test
    void updatedAt_shouldBeSet_onPersistAndUpdate() throws Exception { // TODO: 테스트해볼것, Import auditing class 필요한지 체크

        User user = userRepository.save(User.create("name", "email@emao.com", "password", null));
        Instant first = user.getUpdatedAt();

        Thread.sleep(5); // 5ms 정도 간격
        user.update(new UserUpdateParams("newNickname", null , null, null));
        userRepository.flush();

        Instant second = user.getUpdatedAt();

        assertThat(first).isNotNull();
        assertThat(second).isAfter(first);
    }
}
