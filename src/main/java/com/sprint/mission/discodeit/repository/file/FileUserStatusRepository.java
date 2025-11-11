package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileUserStatusRepository implements UserStatusRepository {

    // 메모리 저장소
    private final Map<UUID, UserStatus> statusMap = new HashMap<>();
    // 파일 저장 경로
    private static final String FILENAME
            = "D:\\codeit07\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\usersStatus.sav";

    private FileUserStatusRepository(){
        loadUserStatusFromFile();
    }

    // 파일 갖고 오기(역직렬화)
    private void loadUserStatusFromFile(){
        File file = new File(FILENAME);
        if(!file.exists()) {
            return; //파일 없으면 빈 Map으로 제작
        }

        try(ObjectInputStream ois
                    = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> map){
                for(Object key : map.keySet()){
                    if(key instanceof UUID && map.get(key) instanceof  UserStatus userStatus){
                        statusMap.put((UUID) key, userStatus);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("파일로드실패");
            e.printStackTrace();
        }
    }

    //직렬화
    private void saveUserStatusToFile(){
        try(ObjectOutputStream oos
                    = new ObjectOutputStream(new FileOutputStream((FILENAME)))){
            oos.writeObject(this.statusMap);
        }catch (Exception e){
            System.out.println("저장 실패");
            e.printStackTrace();
        }
    }

    @Override
    public void save(UserStatus userStatus) {
        statusMap.put(userStatus.getUserId(), userStatus);
        saveUserStatusToFile();
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return statusMap.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst().orElse(null);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(statusMap.values());
    }

    @Override
    public void delete(UUID uuid) {
        statusMap.remove(uuid);
        saveUserStatusToFile();
    }
}
