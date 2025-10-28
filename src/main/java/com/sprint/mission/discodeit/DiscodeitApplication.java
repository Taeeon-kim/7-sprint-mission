package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
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

    static void runUser(UserService userService) {
        // User 등록
        UserCreateRequestDto[] userCreateRequestDtos = new UserCreateRequestDto[] {
                new UserCreateRequestDto("test00", "alice123@gmail.com", "pass123", "Alice", null),
                new UserCreateRequestDto("test02", "name000@gmail.com", "0000pass", "Bob", null),
                new UserCreateRequestDto("test03", "chilysource@gmail.com", "12341234", "Chily", null),
                new UserCreateRequestDto("test05", "tomtom00@gmail.com", "pw123456", "Tom", null)
        };
        for(UserCreateRequestDto userCreateRequestDto : userCreateRequestDtos) {
            userService.createUser(userCreateRequestDto);
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DiscodeitApplication.class, args);

        BasicUserService userService = ctx.getBean(BasicUserService.class);
        BasicChannelService channelService = ctx.getBean(BasicChannelService.class);
        BasicMessageService messageService = ctx.getBean(BasicMessageService.class);

        runUser(userService);
//        User[] users = runUser(userService);
        Channel[] channels = channelService.runChannelService();
        messageService.runMessageService(userService.readAllUser().stream().map(u->new User(u.getUsername(), u.getEmail(), null, u.getNickname(), null))
                .toArray(User[]::new), channels);
    }

}
