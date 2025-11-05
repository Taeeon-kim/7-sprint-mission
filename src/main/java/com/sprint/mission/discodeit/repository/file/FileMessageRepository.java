package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileMessageRepository implements MessageRepository {

    //파일 저장 경로
    private static final String FILENAME
            = "D:\\codeit07\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\message.sav";

    private FileMessageRepository(){
        loadMessageFromFile();
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
                        messages.put((UUID) key, m);
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
            oos.writeObject(this.messages);
        } catch (Exception e) {
            System.out.println("저장 실패");
            e.printStackTrace();
        }
    }

    // 임시저장
    private final Map<UUID, Message> messages = new LinkedHashMap<>();

    @Override
    public void save(Message message) {
        messages.put(message.getUuid(), message);
        saveMessageToFile();
    }

    @Override
    public Optional<Message> findByMessage(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        return List.of();
    }

    @Override
    public List<Message> findAllByChannelId(Channel channel) {
        return List.of();
    }

    @Override
    public Optional<Instant> findLastByChannel(UUID channelId) {
        return messages.values().stream()
                .filter(m->m.getChannelId().equals(channelId))
                .map(Message::getCreateAt)
                .max(Comparator.naturalOrder());
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {

    }

    @Override
    public void deleteMessage(UUID uuid) {
        Message message = messages.get(uuid);
        messages.remove(uuid);
        saveMessageToFile();
    }

//    @Override
//    public Message findByMessage(UUID uuid) {
//        return messages.get(uuid);
//    }
//
//    @Override
//    public List<Message> findUserAll(User user) {
//        return messages.values().stream()
//                .filter(m->m.getUserId().equals(user.getUserId()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Message> findChannelAll(Channel channel) {
//        return messages.values().stream()
//                .filter(m->m.getChannelId().equals(channel.getUuid()))
//                .collect(Collectors.toList());
//    }


//    @Override
//    public void updateMessage(UUID uuid, String newMessage) {
//        Message m = messages.get(uuid);
//        if( m != null){
//            m.setInputMsg(newMessage);
//            saveMessageToFile();
//        }
//    }

}
