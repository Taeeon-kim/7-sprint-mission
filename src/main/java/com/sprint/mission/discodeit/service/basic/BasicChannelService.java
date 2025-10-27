package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;

    public BasicChannelService(ChannelRepository channelRepository, MessageRepository messageRepository, UserReader userReader, ChannelReader channelReader) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.userReader = userReader;
        this.channelReader = channelReader;
    }

    @Override
    public void createChannel(String title, String description, UUID createdByUserId) {
        if (title == null || title.isBlank() || description == null || description.isBlank() || createdByUserId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        User user = userReader.findUserOrThrow(createdByUserId);
        Channel channel = new Channel(title, description, user.getId(), false);
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
            channelById.setUpdatedAt(Instant.now());
            channelRepository.save(channelById);
        }

    }


    @Override
    public void deleteChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = getChannel(channelId);
        // 메세지 레포지토리에서 삭제 로직
        List<UUID> channelMessageIds = channel.getMessageIds();
        for (UUID messageId : channelMessageIds) {
            messageRepository.deleteById(messageId);
        }
        // 채널삭제
        channelRepository.deleteById(channel.getId());
    }

    @Override
    public Channel getChannel(UUID channelId) {
        return channelReader.findChannelOrThrow(channelId);
    }

    @Override
    public void joinChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);

        User user = userReader.findUserOrThrow(userId);
        channel.addUser(user.getId());
        channelRepository.save(channel);
        // TODO: User에서는 따로 channnelIds 가없는데 messagesIds처럼 필요한지 검토필요

    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }

        Channel channel = channelReader.findChannelOrThrow(channelId);

        User user = userReader.findUserOrThrow(userId);
        channel.removeUserId(user.getId());
        channelRepository.save(channel);
        // TODO: User에서는 따로 channnelIds 가없는데 messagesIds처럼 필요한지 검토필요

    }

    @Override
    public List<User> getAllMembers(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }

        Channel channel = channelReader.findChannelOrThrow(channelId);
        List<UUID> userIds = List.copyOf(channel.getUserIds());
        return userReader.findUsersByIds(userIds);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> getChannelsByUserId(UUID userId) {
        User userById = userReader.findUserOrThrow(userId);
        List<Channel> allChannels = getAllChannels();
        return allChannels.stream()
                .filter(channel -> channel.isMember(userById.getId()))
                .toList();
    }
}
