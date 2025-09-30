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
        this.userService = Objects.requireNonNull(userService, "userService must not be null");
        data = new HashMap<>();
    }


    @Override
    public void createChannel(String title, String description, UUID createdByUserId) {
        if (title == null || title.isBlank() || description == null || description.isBlank()) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        userService.getUserById(createdByUserId);
        Channel channel = new Channel("첫 채널 타이틀", "첫 채널입니다 마음껏 메세지를 주고받으세요", createdByUserId, false);
        data.put(channel.getId(), channel);
    }

    @Override
    public void updateChannel(UUID channelId, String title, String description) {
        if (channelId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channelById = null;

        try {
            channelById = getChannel(channelId);
            boolean changeFlag = false;
            changeFlag |= channelById.updateTitle(title);
            changeFlag |= channelById.updateDescription(description);
            if (changeFlag) {
                channelById.setUpdatedAt(System.currentTimeMillis());
            }


        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }


    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (channelId == null) { // TODO: 인메모리라 Null 체크하는지 실제에선 안해도되는지 고려
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        data.remove(channelId);
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
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channel = getChannel(channelId);
        userService.getUserById(userId);
        channel.addUser(userId);
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channel = getChannel(channelId);
        channel.removeUserId(userId);
    }

    @Override
    public List<User> getAllMembers(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
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
        if (userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        User userById = userService.getUserById(userId);
        return getAllChannels().stream()
                .filter(channel -> channel.isMember(userById.getId()))
                .toList();
    }
}
