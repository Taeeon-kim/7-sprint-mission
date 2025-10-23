package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.store.Store;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository {

    @Override
    public void save(Channel channel) {
        Map<UUID, Channel> allChannels = findAllMap();
        allChannels.put(channel.getId(), channel);
        Store.saveMap(Store.CHANNEL_DATA_FILE, allChannels);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Map<UUID, Channel> allMap = findAllMap();

        return Optional.ofNullable(allMap.get(id));
    }

    @Override
    public Map<UUID, Channel> findAllMap() {
        return Store.loadMap(Store.CHANNEL_DATA_FILE);
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> allChannels = Store.loadMap(Store.CHANNEL_DATA_FILE);
        return allChannels.values()
                .stream()
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, Channel> allChannels = findAllMap();
        Channel remove = allChannels.remove(id);
        if (remove != null) {
            Store.saveMap(Store.CHANNEL_DATA_FILE, allChannels);
            return;
        }
        throw new IllegalStateException("failed to delete channel");
    }
}
