package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    //의존성 주입
    private final UserService userService;
    private final MessageRepository messageRepository;

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

    public void runMessageService(User[] users){
        // 메시지 전송
        Message[] msgs = {
                new Message(users[0].getUuid(), users[1].getUuid(), "안녕!"),
                new Message(users[1].getUuid(), users[0].getUuid(), "응, 안녕!"),
                new Message(users[1].getUuid(), users[0].getUuid(), "오늘 뭐해?"),
                new Message(users[0].getUuid(), users[1].getUuid(), "오늘 아무것도 안 해!"),
                new Message(users[1].getUuid(), users[0].getUuid(), "그럼 영화보러갈래?"),
        };
        for (Message m : msgs) {
            createMsg(m);
        };

        // 메시지 전체 조회(목록)
        messageList(users);

        //메시지 수정
        updateMsg(msgs[3].getUuid(), "산책할 거 같아!" + "(수정됨)");

        //메시지 삭제
        deleteMsg(msgs[4].getUuid());

        //다시 조회
        messageList(users);
    }

    //Message 조회
    public void messageList(User[] users) {

        List<Message> userMsg = getAllMsg(users[0]);

        System.out.println(users[0].getNickName() + "의 DM");

        if (userMsg.isEmpty()) {
            System.out.println("(대화없음)");
        }

        for (Message m : userMsg) {
            String messageContnet = m.getInputMsg();
            System.out.println((userService.readUser(m.getSendUser()).getNickName()) + " : " + messageContnet);
        }
    }
}
