package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "File"
)
public class FileBinaryContentRepository implements BinaryContentRepository {

    // 파일 저장 경로
    private static final String FILENAME
            = "D:\\codeit07\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\binaryContent.sav";

    private FileBinaryContentRepository() {
        loadBinaryContentFromFile();
    }

    // 파일 갖고 오기(역직렬화)
    private void loadBinaryContentFromFile(){
        File file = new File(FILENAME);
        if(!file.exists()) {
            return; //파일 없으면 빈 Map으로 제작
        }

        try(ObjectInputStream ois
                    = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> map){
                for(Object key : map.keySet()){
                    if(key instanceof UUID && map.get(key) instanceof  BinaryContent binaryContent){
                        binaryContentMap.put((UUID) key, binaryContent);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("파일로드실패");
            e.printStackTrace();
        }
    }

    //직렬화
    private void saveBinaryContentToFile(){
        try(ObjectOutputStream oos
                    = new ObjectOutputStream(new FileOutputStream((FILENAME)))){
            oos.writeObject(this.binaryContentMap);
        }catch (Exception e){
            System.out.println("저장 실패");
            e.printStackTrace();
        }
    }

    private final Map<UUID, BinaryContent> binaryContentMap = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        // 메모리 저장
        binaryContentMap.put(binaryContent.getUuid(), binaryContent);
        saveBinaryContentToFile();
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID uuid) {
        return binaryContentMap.get(uuid);
    }

    @Override
    public List<BinaryContent> findAllById(UUID uuid) {
        return binaryContentMap.values().stream()
                .filter(content->uuid.equals((content.getUuid())))
                .toList();
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(binaryContentMap.values());
    }

    @Override
    public void delete(UUID uuid) {
        BinaryContent removed = binaryContentMap.remove(uuid);
        saveBinaryContentToFile();
    }
}
