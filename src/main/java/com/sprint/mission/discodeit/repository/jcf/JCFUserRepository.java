package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true)
@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean deleteById(UUID id) {
        return data.remove(id) != null;

    }

    @Override
    public Map<UUID, User> findAllMap() {
        return data;
    }

    @Override
    public List<User> findAll() {
        return data.values()
                .stream()
                .sorted(Comparator.comparing(User::getCreatedAt))
                .toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAllByIds(List<UUID> ids) {
        return ids.stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findAllMap()
                .values()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return findAllMap()
                .values()
                .stream()
                .anyMatch(user -> user.getNickname().equals(nickname));
    }
}
