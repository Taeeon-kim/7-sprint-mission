package com.sprint.mission.discodeit.integration.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest // 내부 @Trnsactional 있어서 자동 롤백
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // DataJpaTest쓸때 자동으로 임베디드 DB를 검색하는데 testImplementation 에 설정한걸로 안하고 runtimeOnly 실행하고자 넣어야됨
public class ChannelRepositoryIntegrationTest {
}
