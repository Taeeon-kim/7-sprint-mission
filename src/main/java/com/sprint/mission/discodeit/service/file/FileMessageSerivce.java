package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageSerivce implements MessageService {
    //파일 저장 경로
    private static final String FILENAME
            = "D:\\codeit07\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\service\\file\\message.sav";

    // 임시저장
    private final Map<UUID, Message> message = new LinkedHashMap<>();

    //싱글톤
    private static final FileMessageSerivce INSTANCE = new FileMessageSerivce();

    private FileMessageSerivce(){
        loadMessageFromFile();
    }

    public static FileMessageSerivce getInstance(){
        return INSTANCE;
    }

    // 파일 갖고 오기(역직렬화)
    private void loadMessageFromFile(){
        File file = new File(FILENAME);
        if(!file.exists()){
            return;
        }

        try(ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(file))){
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> map){
                for(Object key : map.keySet()){
                    if(key instanceof UUID && map.get(key) instanceof Message m){
                        message.put((UUID) key, m);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("파일로드실패");
            e.printStackTrace();
        }
    }

    // 직렬화
    private void saveMessageToFile(){
        try(ObjectOutputStream oos
                    =new ObjectOutputStream(new FileOutputStream(FILENAME))){
            oos.writeObject(this.message);
        } catch (Exception e) {
            System.out.println("저장 실패");
            e.printStackTrace();
        }
    }

    @Override
    public void createMsg(Message msg) {
        message.put(msg.getId(), msg);
        saveMessageToFile();
        System.out.println("[DM전송] " + msg);
    }

    @Override
    public Message getMsg(UUID uuid) {
        return message.get(uuid);
    }

    @Override
    public List<Message> getAllMsg(User user) {
        return message.values().stream()
                .filter(m->m.getSendUser().equals(user.getId())
                        || m.getReceiverUser().equals(user.getId()))
                .sorted(Comparator.comparingLong(Message::getCreateAt))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMsg(UUID uuid, String newMsg) {
        Message m = message.get(uuid);
        if (m != null) {
            m.setInputMsg(newMsg);
            saveMessageToFile();
        }
    }

    @Override
    public void deleteMsg(UUID uuid) {
        message.remove(uuid);
        saveMessageToFile();
    }
}
