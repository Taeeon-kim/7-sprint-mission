package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final UserService userService;
    private final ChannelRepository channelRepository;

    public FileChannelService(UserService userService, ChannelRepository channelRepository) {
        this.userService = userService;
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(String title, String description, UUID createdByUserId) {
        if (
                title == null ||
                        title.isBlank() ||
                        description == null ||
                        description.isBlank() ||
                        createdByUserId == null
        ) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        userService.getUserById(createdByUserId);
        Channel channel = new Channel(title, description, createdByUserId, false);
        channelRepository.save(channel);
    }

    @Override
    public void updateChannel(UUID channelId, String title, String description) {
        if (channelId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        Channel channelById = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));
        boolean changeFlag = false;
        changeFlag |= channelById.updateTitle(title);
        changeFlag |= channelById.updateDescription(description);
        if (changeFlag) {
            channelById.setUpdatedAt(System.currentTimeMillis());
            channelRepository.save(channelById);
        }

    }

    @Override
    public void addMessageToChannel(Channel channel, UUID messageId) {
        if (channel == null || messageId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        channel.addMessageId(messageId);
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        channelRepository.deleteById(channelId);
    }

    @Override
    public Channel getChannel(UUID channelId) {
        return channelRepository
                .findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));
    }

    @Override
    public void joinChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = channelRepository
                .findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        userService.getUserById(userId);
        channel.addUser(userId);
        channelRepository.save(channel);

    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }

        Channel channel = channelRepository
                .findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다"));

        userService.getUserById(userId);
        channel.removeUserId(userId);
        channelRepository.save(channel);

    }

    @Override
    public List<User> getAllMembers(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }

        Channel channel = channelRepository
                .findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        List<UUID> userIds = channel.getUserIds();
        return userService.getUsersByIds(userIds);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> getChannelsByUserId(UUID userId) {
        User userById = userService.getUserById(userId);
        List<Channel> allChannels = getAllChannels();
        return allChannels.stream()
                .filter(channel -> channel.isMember(userById.getId()))
                .toList();
    }
}
