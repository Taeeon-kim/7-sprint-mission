package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
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
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    @Override
    public void createMessage(Message message) {
        if(message == null){
            System.out.println("내용을 입력해주세요");
            return;
        }
        if(message.getSenderId() == null || message.getChannelId() == null){
            System.out.println("작성자나 게시판이 잘못되었습니다.");
            return;
        }
        messageRepository.save(message);
        System.out.println("[메시지 등록]" + message);
    }

    @Override
    public Message getMessage(UUID uuid) {
        if (uuid == null) {
            System.out.println("조회불가");
            return null;
        }
        return messageRepository.findByMessage(uuid);
    }

    @Override
    public List<Message> getUserAllMessage(User user) {
        if(user == null){
            return List.of();
        }
        return messageRepository.findUserAll(user);
    }

    @Override
    public List<Message> getChannelAllMessage(Channel channel) {
        if(channel == null){
            return List.of();
        }
        return messageRepository.findChannelAll(channel);
    }

    @Override
    public void updateMessage(UUID uuid, String newMessage) {
        if(uuid == null){
            System.out.println("수정할 메시지를 찾을 수 없습니다.");
            return;
        }
        if(newMessage == null){
            System.out.println("공백으로 작성할 수 없습니다.");
            return;
        }
        messageRepository.updateMessage(uuid, newMessage);
    }

    @Override
    public void deleteMessage(UUID uuid) {
        if(uuid == null){
            System.out.println("삭제할 메시지가 없습니다");
            return;
        }
        messageRepository.deleteMessage(uuid);
        System.out.println("메시지 삭제");
    }

    public void runMessageService(User[] users, Channel[] channels){
        // 메시지 전송
        Message[] msgs = {
                new Message(users[0].getUuid(), channels[0].getUuid(), "채널 테스트 중"),
                new Message(users[0].getUuid(), channels[0].getUuid(), "이 채널은 이제 제 겁니다"),
                new Message(users[1].getUuid(), channels[0].getUuid(), "어림도 없지!"),
                new Message(users[1].getUuid(), channels[0].getUuid(), "나도 이 채널을 점령한다!"),
                new Message(users[0].getUuid(), channels[0].getUuid(), "헉 🤨🤨🤨"),
        };
        for (Message m : msgs) {
            createMessage(m);
        };

        // 메시지 전체 조회(목록)
//        messageList(users);

        //메시지 수정
        updateMessage(msgs[3].getUuid(), "관심이 없어졌어" + "(수정됨)");

        //메시지 삭제
        deleteMessage(msgs[4].getUuid());

        //다시 조회
//        messageList(users);
    }

    //Message 조회
//    public void messageList(User[] users) {
//
//        List<Message> userMsg = getMessage();
//
//        System.out.println(users[0].getNickName() + "작성글");
//
//        if (userMsg.isEmpty()) {
//            System.out.println("(대화없음)");
//        }
//
//        for (Message m : userMsg) {
//            String messageContnet = m.getInputMsg();
//            System.out.println((userService.readUser(m.getSenderId()).getNickName()) + " : " + messageContnet);
//        }
//    }
}
