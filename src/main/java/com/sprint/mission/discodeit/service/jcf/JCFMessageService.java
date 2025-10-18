package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final MessageRepository repository = JCFMessageRepository.getInstance();

    private JCFMessageService() {
        System.out.println("생성자 private");
    }

    private static final JCFMessageService INSTANCE = new JCFMessageService();

    public static JCFMessageService getInstance(){
        return INSTANCE;
    }

    @Override
    public void createMsg(Message msg) {
        repository.save(msg);
        System.out.println("[DM전송] : " + msg);
    }

    @Override
    public Message getMsg(UUID uuid) {
        return repository.findByMessage(uuid);
    }

    @Override
    public List<Message> getAllMsg(User user) {
        return repository.findAll(user);
    }

    @Override
    public void updateMsg(UUID uuid, String newMsg) {
        repository.findByMessage(uuid);
    }

    @Override
    public void deleteMsg(UUID uuid) {
        repository.deleteMessage(uuid);
    }
}
