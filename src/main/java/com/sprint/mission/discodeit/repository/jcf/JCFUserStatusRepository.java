package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository(Map<UUID, UserStatus> data) {
        this.data = data;
    }


    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values()
                .stream()
                .filter((userStatus) -> userStatus.getUserId().equals(userId))
                .findFirst();
    }
}
