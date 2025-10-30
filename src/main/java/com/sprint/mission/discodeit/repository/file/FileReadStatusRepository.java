package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    //파일 저장 경로
    private static final String FILENAME
            = "D:\\codeit07\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\readStatus.sav";

    // 임시저장
    private final Map<UUID, ReadStatus> store = new HashMap<>();

    public FileReadStatusRepository() {
        loadFromFile();
    }

    //파일 갖고 오기(역직렬화)
    private void loadFromFile() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois
                     = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> map) {
                for (Object key : map.keySet()) {
                    if (key instanceof UUID && map.get(key) instanceof ReadStatus readStatus) {
                        store.put((UUID) key, readStatus);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("파일로드실패");
            e.printStackTrace();
        }
    }

    // 직렬화
    private void saveToFile() {
        try (ObjectOutputStream oos
                     = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(this.store);
        } catch (Exception e) {
            System.out.println("저장 실패");
            e.printStackTrace();
        }
    }

    @Override
    public void save(ReadStatus readStatus) {
        store.put(readStatus.getUuid(), readStatus);
        saveToFile();
    }

    @Override
    public ReadStatus findByUserAndChannel(UUID userId, UUID channelId) {
        return store.values().stream()
                .filter(rs -> rs.getUserId().equals(userId)
                        && rs.getChannelId().equals(channelId))
                .findFirst().orElse(null);
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return store.values().stream()
                .filter(rs->rs.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return store.values().stream()
                .filter(rs->rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void update(ReadStatus readStatus) {
        store.put(readStatus.getUuid(), readStatus);
        saveToFile();
    }

    @Override
    public void delete(UUID uuid) {
        store.remove(uuid);
        saveToFile();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        List<UUID> toRemove = store.values().stream()
                .filter(rs->rs.getChannelId().equals(channelId))
                .map(rs->rs.getUuid())
                .collect(Collectors.toList());
        toRemove.forEach(store::remove);
        saveToFile();
    }
}
