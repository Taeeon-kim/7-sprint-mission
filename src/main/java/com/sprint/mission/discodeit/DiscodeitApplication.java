package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageSendRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
        // 서비스 초기화
        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.

        final UserService userService = context.getBean(UserService.class);
        final ChannelService channelService = context.getBean(ChannelService.class);
        final MessageService messageService = context.getBean(MessageService.class);

        List<User> allUsers = userService.getAllUsers();
        System.out.println("allUsers = " + allUsers);
        // ---- 유저 만들기 (회원가입) ------
        userService.signUp(new UserSignupRequestDto("youngble", "email@example.com", "password123", "010-1234-5678", null));
        userService.signUp(new UserSignupRequestDto("zzzz", "zzzz@example.com", "zzzz1123", "010-3334-44444", null));
        userService.signUp(new UserSignupRequestDto("Hong", "test@example.com", "wssss123123", "010-1122-3333", null));
        System.out.println("=============================== ");
        allUsers = userService.getAllUsers();
        System.out.println("allUsers = " + allUsers);
        System.out.println("=============================== ");

        // ---- 유저 지우기 (회원 탈퇴) ------
        userService.deleteUser(allUsers.get(0).getId());
//        userService.deleteUser(allUsers.get(0).getId());
        allUsers = userService.getAllUsers();
        System.out.println("allUsers = " + allUsers);

        System.out.println("===========update user==================== ");
        //--- 유저 수정하기(정보 업데이트) -------
        // ----- 클라이언트 요청 api 데이터, 실제 dto 형식으로 받아서 dto.nickname 같은식으로 넘겨줌 ---
        userService.updateUser(allUsers.get(0).getId(),
                new UserUpdateRequestDto("changedName", "changedName!!", null, "changedPassword!!", null));
//        User userById = userService.getUserById(user2.getId());
        allUsers = userService.getAllUsers();
        System.out.println("allUsers = " + allUsers);

        // ------- 채널 조회 ----
        List<ChannelResponseDto> allChannels = channelService.getAllChannels();
        System.out.println("allChannels = " + allChannels);

        System.out.println("=============================== ");

        // ------- 채널 만들기  ----

        channelService.createChannel(allUsers.get(0).getId(), new ChannelCreateRequestDto("첫 채널 타이틀", "첫 채널입니다 마음껏 메세지를 주고받으세요", ChannelType.PUBLIC, null));
        channelService.createChannel(allUsers.get(0).getId(), new ChannelCreateRequestDto("두번째 타이틀", "두번째 채널 공지 채널입니다.", ChannelType.PUBLIC, null));
        allChannels = channelService.getAllChannels();
        System.out.println("all channels after creating channel :" + allChannels);

        System.out.println("=============================== ");

        channelService.updateChannel(allChannels.get(0).channelId(), new ChannelUpdateRequestDto("changed title", "changed description"));
        allChannels = channelService.getAllChannels();
        System.out.println("all channels after updating channel :" + allChannels);
        System.out.println("=============================== ");

        channelService.updateChannel(allChannels.get(0).channelId(), new ChannelUpdateRequestDto(null, "changed description"));
        allChannels = channelService.getAllChannels();
        System.out.println("all channels after updating channel2 :" + allChannels);
        System.out.println("=============================== ");


        // --- 채널 맴버 조회 ----
        List<User> allMembers = channelService.getAllMembers(allChannels.get(0).channelId());
        System.out.println("all members of channel before join channel: " + allMembers);

        System.out.println("=============================== ");

        // --- 채널 조인 ----
        UUID chId = allChannels.get(0).channelId();
        allUsers
                .forEach(member -> {
                    channelService.joinChannel(chId, member.getId());
                });

        allMembers = channelService.getAllMembers(chId);
        System.out.println("all members of channel after join channel: " + allMembers);

        System.out.println("=============================== ");

        System.out.println("user's channel: " + channelService.getChannelsByUserId(allUsers.get(0).getId()));
        System.out.println("=============================== ");
        // -- 채널 나가기 ----
        channelService.leaveChannel(chId, allMembers.get(0).getId());

        allMembers = channelService.getAllMembers(chId);
        System.out.println("all members of channel after leave channel: " + allMembers);
        System.out.println("=============================== ");
        System.out.println("user's channel after leave: " + channelService.getChannelsByUserId(allUsers.get(0).getId()));

        System.out.println("=============================== ");

        // --- 채널의 모든 메세지 확인 ---
        List<Message> allMessagesOfChannel = messageService.getAllMessagesByChannelId(chId);
        System.out.println("chId 1 = " + chId);
        System.out.println("all messages of channel before send message: " + allMessagesOfChannel);

        System.out.println("=============================== ");

        // --- 메세지 보내기 --

        messageService.sendMessageToChannel(MessageSendRequestDto.builder()
                .channelId(chId)
                .senderId(allMembers.get(0).getId())
                .content("첫번째 message입니다")
                .build());

        allMessagesOfChannel = messageService.getAllMessagesByChannelId(chId);
        System.out.println("chId 2 = " + chId);
        System.out.println("all messages of channel after send message: " + allMessagesOfChannel);

        System.out.println("=============================== ");
        System.out.println("user's channel: " + channelService.getChannelsByUserId(allMembers.get(0).getId()));
        System.out.println("=============================== ");


        // -- 모든 메세지 보기 ---
        List<Message> allMessages = messageService.getAllMessages();
        System.out.println("allMessages = " + allMessages);
        System.out.println("=============================== ");
        messageService.sendMessageToChannel(MessageSendRequestDto.builder()
                .channelId(chId)
                .senderId(allMembers.get(0).getId())
                .content("두번째 message입니다")
                .build());
        messageService.sendMessageToChannel(MessageSendRequestDto.builder()
                .channelId(chId)
                .senderId(allMembers.get(0).getId())
                .content("세번째 message입니다")
                .build());
        allMessages = messageService.getAllMessages();
        System.out.println("allMessages after send Message = " + allMessages);
        System.out.println("=============================== ");

        // -- 단일 특정 메세지 보기 --
        Message messageById = messageService.getMessageById(allMessages.get(0).getId());
        System.out.println("messageById = " + messageById);
        System.out.println("=============================== ");

        // -- 메세지 업데이트하기 --
        messageService.updateMessage(messageById.getId(), MessageUpdateRequestDto.builder()
                .content("updated message!!")
                .build());
        messageById = messageService.getMessageById(messageById.getId());
        System.out.println("updated messageById = " + messageById);
        allMessagesOfChannel = messageService.getAllMessagesByChannelId(chId);
        System.out.println("allMessagesOfChannel = " + allMessagesOfChannel);
        // -- 메세지 제거하기 --
        messageService.deleteMessage(messageById.getId());
        allMessages = messageService.getAllMessages();
        System.out.println("allMessages after delecting a message= " + allMessages);
        allMessagesOfChannel = messageService.getAllMessagesByChannelId(chId);
        System.out.println("allMessagesOfChannel after delecting a message= " + allMessagesOfChannel);
        // -- 채널 제거하기 ---
        channelService.deleteChannel(allChannels.get(0).channelId());
        allChannels = channelService.getAllChannels();
        System.out.println("all channels after deleting the channel :" + allChannels);
        allMessages = messageService.getAllMessages();
        System.out.println("allMessages after delecting the channel = " + allMessages);


    }

}
