package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {

    // 파일 저장 경로
    private static final String FILENAME
            = "D:\\codeit07\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\users.sav";

    private FileUserRepository(){
        loadUserFromFile();
    }

    // 파일 갖고 오기(역직렬화)
    private void loadUserFromFile(){
        File file = new File(FILENAME);
        if(!file.exists()) {
            return; //파일 없으면 빈 Map으로 제작
        }

        try(ObjectInputStream ois
                    = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> map){
                for(Object key : map.keySet()){
                    if(key instanceof UUID && map.get(key) instanceof  User u){
                        users.put((UUID) key, u);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("파일로드실패");
            e.printStackTrace();
        }
    }

    //직렬화
    private void saveUserToFile(){
        try(ObjectOutputStream oos
                    = new ObjectOutputStream(new FileOutputStream((FILENAME)))){
            oos.writeObject(this.users);
        }catch (Exception e){
            System.out.println("저장 실패");
            e.printStackTrace();
        }
    }

    // 임시저장
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUuid(), user);
        saveUserToFile();
    }

    @Override
    public User findById(UUID uuid) {
        return users.get(uuid);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(UUID uuid) {
        User user =  users.get(uuid);
        users.remove(uuid);
        saveUserToFile();
    }


//    @Override
//    public List<User> findAll() {
//        return new ArrayList<>(users.values());
//    }
//
//    @Override
//    public void updateNickName(UUID uuid, String newName) {
//        User u = users.get(uuid);
//        if( u != null ){
//            u.setNickName(newName);
//            saveUserToFile();
//        }
//    }
//
//    @Override
//    public void updatePassword(UUID uuid, String newPassword) {
//        User u = users.get(uuid);
//        if( u != null){
//            u.setUserPassword(newPassword);
//            saveUserToFile();
//        }
//    }
}
