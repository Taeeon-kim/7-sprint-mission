package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

public class AppConfig {
    // Service Config
    // NOTE: 싱글톤을 유지하기위해 private final로 한번 생성
    private final UserRepository userRepository = new FileUserRepository();


    private final UserService userService = new FileUserService(userRepository);
    private final ChannelService channelService = new JCFChannelService(userService);
    private final MessageService messageService = new JCFMessageService(userService, channelService);

    // User
    public UserService userService() {
        return userService;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    // Channel
    public ChannelService channelService() {
        return channelService;
    }

    // Message
    public MessageService messageService() {
        return messageService;
    }
}
