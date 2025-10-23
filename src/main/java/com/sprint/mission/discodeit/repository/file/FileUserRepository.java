package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.store.Store;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {


    @Override
    public void save(User user) {
        Map<UUID, User> allUsers = findAllMap();
        allUsers.put(user.getId(), user);
        Store.saveMap(Store.USER_DATA_FILE, allUsers);
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, User> allUsers = findAllMap();
        User remove = allUsers.remove(id);
        if (remove != null) {
            Store.saveMap(Store.USER_DATA_FILE, allUsers);
        }

    }

    @Override
    public Map<UUID, User> findAllMap() {
        return Store.loadMap(Store.USER_DATA_FILE);
    }

    @Override
    public List<User> findAll() {
        return findAllMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(User::getCreatedAt))
                .toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        Map<UUID, User> allUsers = findAllMap();
        return Optional.ofNullable(allUsers.get(id));
    }

    @Override
    public List<User> findAllByIds(List<UUID> ids) {
        Map<UUID, User> allUsers = findAllMap();

        return ids.stream()
                .map(allUsers::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
