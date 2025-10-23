package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {

    //의존성 주입
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMsg(Message msg) {
        messageRepository.save(msg);
        System.out.println("[DM전송] " + msg);
    }

    @Override
    public Message getMsg(UUID uuid) {
        return messageRepository.findByMessage(uuid);
    }

    @Override
    public List<Message> getAllMsg(User user) {
        return messageRepository.findAll(user);
    }

    @Override
    public void updateMsg(UUID uuid, String newMsg) {
        messageRepository.updateMessage(uuid, newMsg);
    }

    @Override
    public void deleteMsg(UUID uuid) {
        messageRepository.deleteMessage(uuid);
        System.out.println("메시지 삭제");
    }
}
