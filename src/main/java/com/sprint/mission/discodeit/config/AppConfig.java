package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

public class AppConfig {
    // Service Config
    // NOTE: 싱글톤을 유지하기위해 private final로 한번 생성
    private final UserRepository userRepository = new FileUserRepository();
    private final ChannelRepository channelRepository = new FileChannelRepository();
    private final MessageRepository messageRepository = new FileMessageRepository();

    private final UserService userService = new FileUserService(userRepository);
    private final ChannelService channelService = new FileChannelService(userService, channelRepository, messageRepository);
    private final MessageService messageService = new FileMessageService(userService, channelService, messageRepository, channelRepository);


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

    public ChannelRepository channelRepository() {
        return channelRepository;
    }

    // Message
    public MessageService messageService() {
        return messageService;
    }
}
