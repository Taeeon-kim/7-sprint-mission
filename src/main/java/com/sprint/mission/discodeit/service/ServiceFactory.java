package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class ServiceFactory {

    private ServiceFactory() {}

    public static UserService getUserService(){
        return FileUserService.getInstance();
    }

    public static ChannelService getChannelService(){
        return FileChannelService.getInstance();
    }

    public static MessageService getMessageService(){
        return JCFMessageService.getInstance();
    }

}
