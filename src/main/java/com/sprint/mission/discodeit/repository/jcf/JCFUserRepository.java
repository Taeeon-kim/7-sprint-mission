package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true // 기본값: jcf
)
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUuid(),  user);
    }

    @Override
    public Optional<User> findById(String userId) {
        return users.values().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return Optional.ofNullable(users.get(uuid));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(UUID uuid) {
        users.remove(uuid);
    }
}
