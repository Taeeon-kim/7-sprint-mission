package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // 채널들을 담을 리스트(맵)
    private final Map<UUID, Channel> data;
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.userService = userService;
        data = new HashMap<>();
    }


    @Override
    public void createChannel(String title, String description, UUID createdByUserId) {
        userService.getUserById(createdByUserId);
        Channel channel = new Channel("첫 채널 타이틀", "첫 채널입니다 마음껏 메세지를 주고받으세요", createdByUserId, false);
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
        userService.getUserById(userId);
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
        return data.values()
                .stream()
                .toList();
    }


    @Override
    public List<Channel> getChannelsByUserId(UUID userId) {
        return List.of();
    }
}
