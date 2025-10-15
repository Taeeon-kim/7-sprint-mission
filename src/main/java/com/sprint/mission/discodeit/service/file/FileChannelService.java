package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    //파일 저장 경로
    private static final String FILENAME
            = "D:\\codeit07\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\service\\file\\channels.sav";

    // 임시저장
    private final Map<UUID, Channel> channels = new HashMap<>();

    // 싱글톤
    private static final FileChannelService INSTANCE = new FileChannelService();

    private FileChannelService(){
        loadChannelFromFile();
    }

    public static FileChannelService getInstance(){
        return INSTANCE;
    }

    //파일 갖고 오기(역직렬화)
    private void loadChannelFromFile(){
        File file = new File(FILENAME);
        if(!file.exists()){
            return;
        }

        try(ObjectInputStream ois
                    = new ObjectInputStream(new FileInputStream(file))){
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> map){
                for(Object key : map.keySet()){
                    if(key instanceof UUID && map.get(key) instanceof Channel c){
                        channels.put((UUID) key, c);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("파일로드실패");
            e.printStackTrace();
        }
    }

    // 직렬화
    private void saveChannelToFile(){
        try(ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream(FILENAME))){
            oos.writeObject(this.channels);
        }catch (Exception e){
            System.out.println("저장 실패");
            e.printStackTrace();
        }
    }

    @Override
    public void createChannel(Channel channel) {
        channels.put(channel.getId(), channel);
        saveChannelToFile();
        System.out.println("[Channel 생성] : " + channel);
    }

    @Override
    public Channel readChannel(UUID uuid) {
        return channels.get(uuid);
    }

    @Override
    public List<Channel> readAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void updateChannel(UUID uuid, String newName) {
        Channel ch = channels.get(uuid);
        if(ch != null) {
            ch.setChanName(newName);
            saveChannelToFile();
        }
    }

    @Override
    public void deleteChannel(UUID uuid) {
        Channel ch = channels.remove(uuid);
        System.out.println("[Channel 삭제] : " + ch);
        saveChannelToFile();
    }
}
