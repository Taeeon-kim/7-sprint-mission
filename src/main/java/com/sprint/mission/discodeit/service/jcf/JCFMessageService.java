package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messages = new LinkedHashMap<>();

    private JCFMessageService() {
        System.out.println("생성자 private");
    }

    private static final JCFMessageService INSTANCE = new JCFMessageService();

    public static JCFMessageService getInstance(){
        return INSTANCE;
    }

    @Override
    public void createMsg(Message msg) {
        messages.put(msg.getId(), msg);
        System.out.println("[DM전송] : " + msg);
    }

    @Override
    public Message getMsg(UUID uuid) {
        return messages.get(uuid);
    }

    @Override
    public List<Message> getAllMsg(User user) {
        return messages.values().stream()
                .filter(m->m.getSendUser().equals(user.getId()) || m.getReceiverUser().equals(user.getId()))
                .sorted(Comparator.comparingLong(Message::getCreateAt))
                .collect(Collectors.toList());

//        List<Message> result = new ArrayList<>();
//        for(Message m : messages.values()){
//            // user가 보낸 메시지 or user가 받은 메시지 둘 다 포함
//            if(m.getSendUser().equals(user.getId()) || m.getReceiverUser().equals(user.getId())){
//                result.add(m);
//            }
//        }
//        result.sort(Comparator.comparingLong(Message::getCreateAt));
//        return result;
    }

    @Override
    public void updateMsg(UUID uuid, String newMsg) {
        Message m = messages.get(uuid);
        if ( m != null) m.setInputMsg(newMsg);
    }

    @Override
    public void deleteMsg(UUID uuid) {
        messages.remove(uuid);
    }
}
