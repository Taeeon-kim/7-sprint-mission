package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sun.source.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DiscodeitApplication.class, args);

        BasicUserService userService = ctx.getBean(BasicUserService.class);
        BasicChannelService channelService = ctx.getBean(BasicChannelService.class);
        BasicMessageService messageService = ctx.getBean(BasicMessageService.class);
        BasicAuthService  authService = ctx.getBean(BasicAuthService.class);

        userService.runTest();
        authService.runAuthTest();
        channelService.runChannelTest();
//        Channel[] channels = channelService.runChannelService();
        List<User> readUsers = userService.getReadUsers();
        List<Channel> readChannel = channelService.getReadChannel();
        messageService.runMessageService(readUsers.toArray(new User[0]), readChannel.toArray(new Channel[0]));
    }

}
