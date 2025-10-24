package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository(HashMap<UUID, User> data) {
        this.data = data;
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
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
}
