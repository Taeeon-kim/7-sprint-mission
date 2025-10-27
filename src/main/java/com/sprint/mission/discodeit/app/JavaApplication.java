//package com.sprint.mission.discodeit.app;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;
//
///**
// * 프로그램 실행 테스트
// * - User, Channel, 1:1 DM 테스트
// */
//public class JavaApplication {
//
//    // 서비스 객체 생성
//    static UserService userService;
//    static ChannelService channelService;
//    static MessageService messageService;
//
//    public static void main(String[] args) {
//
//        // User 등록
//        User[] users = {
//                new User("test00", "alice123@gmail.com","pass123", "Alice"),
//                new User("test02", "name000@gmail.com","0000pass", "Bob"),
//                new User("test03", "chilysource@gmail.com", "12341234", "Chily"),
//                new User("test05", "tomtom00@gmail.com","pw123456", "Tom")
//        };
//        for (User u : users) {
//            userService.createUser(u);
//        }
//
//        // 유저 전체 조회
//        userList();
//
//        // 유저 닉네임 수정 Bob->Minsu
//        userService.updateUser(users[1].getUuid(), "Minsu");
//
//        // 유저 password 수정 Bob : 0000pass -> 012456pw
//        userService.updatePassword(users[1].getUuid(), "012456pw");
//
//        // 유저 단건 조회
//        System.out.println("[유저 검색] : " + userService.readUser(users[1].getUuid()));
//
//        // 유저 삭제
//        userService.deleteUser(users[3].getUuid());
//        System.out.println("탈퇴 : " + users[3].getNickName() + "님");
//
//        // 전체 조회
//        userList();
//
//        System.out.println("───────────────────────────────────────────────────────────");
//
//        // 채널 생성
//        Channel[] channels = {
//                new Channel(PUBLIC, "이벤트"),
//                new Channel(PUBLIC, "공지"),
//                new Channel(PUBLIC,"자유게시판")
//        };
//        for (Channel c : channels) {
//            channelService.createChannel(c);
//        }
//
//        // 채널 조회
//        System.out.println("[채널 검색] : " + channelService.readChannel(channels[0].getUuid()));
//
//        // 채널명 수정
//        channelService.updateChannel(channels[0].getUuid(), "공지 및 이벤트");
//        channelService.updateChannel(channels[1].getUuid(), "Q & A");
//
//        // 채널 전체 조회
//        channelList();
//
//        // 채널 삭제
//        channelService.deleteChannel(channels[1].getUuid());
//        // 채널 전체 조회
//        channelList();
//
//        System.out.println("───────────────────────────────────────────────────────────");
//
//        // 메시지 전송
//        Message[] msgs = {
//                new Message(users[0].getUuid(), users[1].getUuid(), "안녕!"),
//                new Message(users[1].getUuid(), users[0].getUuid(), "응, 안녕!"),
//                new Message(users[1].getUuid(), users[0].getUuid(), "오늘 뭐해?"),
//                new Message(users[0].getUuid(), users[1].getUuid(), "오늘 아무것도 안 해!"),
//                new Message(users[1].getUuid(), users[0].getUuid(), "그럼 영화보러갈래?"),
//        };
//        for (Message m : msgs) {
//            messageService.createMessage(m);
//        };
//
//        // 메시지 전체 조회(목록)
//        messageList(users);
//
//        //메시지 수정
//        messageService.updateMessage(msgs[3].getUuid(), "산책할 거 같아!" + "(수정됨)");
//
//        //메시지 삭제
//        messageService.deleteMessage(msgs[4].getUuid());
//
//        //다시 조회
//        messageList(users);
//    }
//
//    //유저 전체 조회
//    public static void userList() {
//        System.out.println("[유저 전체 조회]");
//        Set<String> userSet = new HashSet<>();
//        for (User u : userService.readAllUser()) {
//            if (userSet.add(u.getUserId())) { // userId 기준
//                System.out.println("ID: " + u.getUserId() + " / Name: " + u.getNickName());
//            }
//        }
//    }
//
//    //채널 전체 조회
//    public static void channelList() {
//        System.out.println("[채널 전체 조회]");
//        Set<String> channelSet = new HashSet<>();
//        for (Channel c : channelService.readAllChannels()) {
//            if(channelSet.add(c.getChanName())){
//                System.out.println("채널명: " + c.getChanName());
//            }
//        }
//    }
//
//    //Message 조회
//    public static void messageList(User[] users) {
//
//        List<Message> userMsg = messageService.getUserAllMessage(users[0]);
//
//        System.out.println(users[0].getNickName() + "의 DM");
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
//
//
//}
