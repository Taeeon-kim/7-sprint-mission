package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {

    // 파일 이름 정의
    private static final String FILENAME
            = "D:\\codeit07\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\users.sav";

    // 메모리 캐시
    private final Map<UUID, User> users = new HashMap<>();

    //싱글톤
    private static final FileUserService INSTANCE = new FileUserService();

    private FileUserService(){
        loadUserFromFile();
    }

    public static FileUserService getInstance(){
        return INSTANCE;
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


    @Override
    public void createUser(User user) {
        if(users.containsKey(user.getId())){
            return;
        }

        users.put(user.getId(), user);
        saveUserToFile();
        System.out.println("[User 생성 및 저장 완료] : " + user);
    }

    @Override
    public User readUser(UUID uuid) {
        return users.get(uuid);
    }

    @Override
    public List<User> readAllUser() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void updateUser(UUID uuid, String newName) {
        User u = users.get(uuid);
        if( u != null){
            u.setNickName(newName);
            saveUserToFile();
        }
    }

    @Override
    public void updatePw(UUID uuid, String newPassword) {
        User u = users.get(uuid);
        if( u != null ) {
            u.setUserPassword(newPassword);
            saveUserToFile();
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        users.remove(uuid);
        saveUserToFile();
    }
}
