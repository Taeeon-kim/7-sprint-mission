package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageSerivce implements MessageService {

    //의존성 주입
    private final MessageRepository messageRepository = FileMessageRepository.getInstance();

    //싱글톤
    private static final FileMessageSerivce INSTANCE = new FileMessageSerivce();

    private FileMessageSerivce(){

    }

    public static FileMessageSerivce getInstance(){
        return INSTANCE;
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
