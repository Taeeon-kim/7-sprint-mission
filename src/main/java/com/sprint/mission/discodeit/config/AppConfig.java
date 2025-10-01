package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class AppConfig {
    // Service Config
    // NOTE: 싱글톤을 유지하기위해 private final로 한번 생성
    private final UserService userService = new JCFUserService();
    private final ChannelService channelService = new JCFChannelService(userService);
    private final MessageService messageService = new JCFMessageService(userService, channelService);

    // User
    public UserService userService() {
        return userService;
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
