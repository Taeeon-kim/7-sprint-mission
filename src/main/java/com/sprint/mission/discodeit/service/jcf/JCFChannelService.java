package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // 채널들을 담을 리스트(맵)
    private final Map<UUID, Channel> data;
    private final UserService userService;
    private final MessageService messageService;

    public JCFChannelService(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
        data = new HashMap<>();
    }


    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public void updateChannel(Channel channel) {
        Channel channelById = null;

        try {
            channelById = getChannel(channel.getId());
            boolean changeFlag = false;
            changeFlag |= channelById.updateTitle(channel.getTitle());
            changeFlag |= channelById.updateDescription(channel.getDescription());
            if (changeFlag) {
                channelById.setUpdatedAt(System.currentTimeMillis());
            }


        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }


    }

    @Override
    public void deleteChannel(UUID channelId) {

    }

    @Override
    public Channel getChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = data.get(channelId);
        if (channel == null) {
            throw new IllegalStateException("채널이 없습니다");
        }
        return channel;
    }

    @Override
    public void joinChannel(UUID channelId, UUID userId) {
        Channel channel = getChannel(channelId);
        channel.addUser(userId);
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        Channel channel = getChannel(channelId);
        channel.removeUserId(userId);
    }

    @Override
    public List<User> getAllMembers(UUID channelId) {
        Channel channel = getChannel(channelId);
        List<UUID> userIds = channel.getUserIds();
        return userService.getUsersByIds(userIds);
    }

    @Override
    public List<Channel> getAllChannels() {
        return data.values().stream().toList();
    }

    @Override
    public List<Message> getAllMessages(UUID channelId) {
        Channel channel = getChannel(channelId);
        List<UUID> messageIds = channel.getMessageIds();
        return messageService.getAllMessagesByIds(messageIds); // TODO: 인메모리사용시 불변리스트 반환 안해도되는지,,
    }

    @Override
    public List<Channel> getChannelsByUserId(UUID userId) {
        return List.of();
    }
}
