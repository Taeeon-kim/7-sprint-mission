package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // 채널들을 담을 리스트(맵)
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;

    public JCFChannelService(ChannelRepository channelRepository, MessageRepository messageRepository, UserReader userReader, ChannelReader channelReader) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.userReader = userReader;
        this.channelReader = channelReader;
    }


    @Override
    public void createChannel(String title, String description, UUID createdByUserId) { // TODO: 추후 컨트롤러 계층생성시 파라미터를 DTO로 변경(파라미터가 길어질시)
        if (
                title == null ||
                        title.isBlank() ||
                        description == null ||
                        description.isBlank() ||
                        createdByUserId == null
        ) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        userReader.findUserOrThrow(createdByUserId);
        Channel channel = new Channel(title, description, createdByUserId, false);
        channelRepository.save(channel);
    }

    @Override
    public void updateChannel(UUID channelId, String title, String description) {// TODO: 추후 컨트롤러 계층생성시 파라미터를 DTO로 변경(파라미터가 길어질시)
        if (channelId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        Channel channelById = channelReader.findChannelOrThrow(channelId);
        boolean changeFlag = false;
        changeFlag |= channelById.updateTitle(title);
        changeFlag |= channelById.updateDescription(description);
        if (changeFlag) {
            channelById.setUpdatedAt(System.currentTimeMillis());
        }


    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (channelId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);
        // 메세지 레포지토리에서 삭제 로직
        List<UUID> channelMessageIds = channel.getMessageIds();
        for (UUID messageId : channelMessageIds) {
            messageRepository.deleteById(messageId);
        }
        channelRepository.deleteById(channel.getId());
    }

    @Override
    public Channel getChannel(UUID channelId) {
        return channelReader.findChannelOrThrow(channelId);
    }


    @Override
    public void joinChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);
        userReader.findUserOrThrow(userId);
        channel.addUser(userId);
        // TODO: User에서는 따로 channnelIds 가없는데 messagesIds처럼 필요한지 검토필요
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);
        channel.removeUserId(userId);
        // TODO: User에서는 따로 channnelIds 가없는데 messagesIds처럼 필요한지 검토필요
    }

    @Override
    public List<User> getAllMembers(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);
        List<UUID> userIds = channel.getUserIds();
        return userReader.findUsersByIds(userIds);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAllMap()
                .values()
                .stream()
                .toList();
    }


    @Override
    public List<Channel> getChannelsByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        User userById = userReader.findUserOrThrow(userId);

        return channelRepository.findAllMap()
                .values()
                .stream()
                .filter(channel -> channel.isMember(userById.getId()))
                .toList();
    }


}
