package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void createMessage(MessageCreateRequestDto messageCreateRequestDto) {
        if (messageCreateRequestDto == null) {
            System.out.println("내용을 입력해주세요");
            return;
        }
        Message message = new Message(messageCreateRequestDto.getSenderId(),
                messageCreateRequestDto.getChannelId(),
                messageCreateRequestDto.getInputMsg());
        createMessage(message);
    }

    // 오버로드
    public void createMessage(Message message) {
        if (message == null) {
            System.out.println("메시지 객체가 없습니다.");
            return;
        }

        if (message.getSenderId() == null || message.getChannelId() == null) {
            System.out.println("작성자나 게시판이 잘못되었습니다.");
            return;
        }
        messageRepository.save(message);

        ChannelResponseDto channelResponseDto = channelService.findChannel(message.getChannelId());
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
    public List<Message> getChannelAllMessage(Channel channel) {
        if (channel == null) {
            return List.of();
        }
        String channelName = channel.getChanName();
        System.out.println("채널 [" + channelName + "] 의 메시지 출력");
        return messageRepository.findChannelAll(channel);
    }

    @Override
    public List<Message> getUserAllMessage(User user) {
        if (user == null) {
            return List.of();
        }
        String nickName = user.getNickName();
        System.out.println("[" + nickName + "] 의 메시지 출력");
        return messageRepository.findUserAll(user);
    }

    @Override
    public void updateMessage(UUID uuid, String newMessage) {
        if (uuid == null) {
            System.out.println("수정할 메시지를 찾을 수 없습니다.");
            return;
        }
        if (newMessage == null) {
            System.out.println("공백으로 작성할 수 없습니다.");
            return;
        }
        messageRepository.updateMessage(uuid, newMessage);
    }

    @Override
    public void deleteMessage(UUID uuid) {
        if (uuid == null) {
            System.out.println("삭제할 메시지가 없습니다");
            return;
        }
        messageRepository.deleteMessage(uuid);
        System.out.println("메시지 삭제");
    }

    public void runMessageService(User[] users, Channel[] channels) {
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
        }
        ;

        // 메시지 전체 조회(채널기준)
        channelMessageList(channels[0]);

        //메시지 수정
        updateMessage(msgs[3].getUuid(), "관심이 없어졌어" + "(수정됨)");

        //메시지 삭제
        deleteMessage(msgs[4].getUuid());

        // 메시지 전체 조회(채널기준)
        channelMessageList(channels[0]);

        // 메시지 전체 조회(유저기준)
        userMessageList(users[0]);

    }

    public void channelMessageList(Channel channel) {
        List<Message> messages = getChannelAllMessage(channel);

        if (messages.isEmpty()) {
            System.out.println("작성된 메시지가 없습니다.");
            return;
        }

        for (Message m : messages) {
            String nickName = userService.readUser(m.getSenderId()).getNickname();//.getNickName();
            String channelName = channelService.findChannel(m.getChannelId()).getChannelName();
            System.out.println(
                    "[" + m.getCreateAt() + "] "
                            + nickName + " - "
                            + channelName + " : "
                            + m.getInputMsg()
            );
        }
    }

    public void userMessageList(User user) {
        List<Message> messages = getUserAllMessage(user);
        if (messages.isEmpty()) {
            System.out.println("작성된 메시지가 없습니다");
            return;
        }
        for (Message m : messages) {
            String nickName = userService.readUser(m.getSenderId()).getNickname(); //.getNickName();
            String channelName = channelService.findChannel(m.getChannelId()).getChannelName();
            System.out.println(
                    "[" + m.getCreateAt() + "] "
                            + nickName + " - "
                            + channelName + " : "
                            + m.getInputMsg()
            );
        }
    }

}
