package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.factory.ChannelFactory;
import com.sprint.mission.discodeit.service.factory.PrivateChannelCreator;
import com.sprint.mission.discodeit.service.factory.PublicChannelCreator;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelFactory channelFactory;

    public BasicChannelService(ChannelRepository channelRepository, MessageRepository messageRepository, UserReader userReader, ChannelReader channelReader, ReadStatusRepository readStatusRepository, ChannelFactory channelFactory) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.userReader = userReader;
        this.channelReader = channelReader;
        this.readStatusRepository = readStatusRepository;
        this.channelFactory = channelFactory;
    }

    @Override
    public ChannelResponseDto createChannel(ChannelCreateCommand command) {

        Channel channel = channelFactory.create(command);

        Channel saved = channelRepository.save(channel);

        if (saved.getType() == ChannelType.PRIVATE) {
            List<User> users = userReader.findUsersByIds(command.memberIds());
            List<ReadStatus> readStatuses = users.stream()
                    .map(user -> new ReadStatus(user, saved, Instant.now())).toList();

            readStatusRepository.saveAll(readStatuses);
        }

        List<UUID> participantIds = readStatusRepository.findByChannelId(channel.getId()).stream()
                .map(readStatus -> readStatus.getUser().getId())
                .collect(Collectors.toList());
        List<UUID> messageIds = messageRepository.findIdsByChannelId(channel.getId());
        Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId())
                .map(Message::getCreatedAt)
                .orElse(null);
        return ChannelResponseDto.from(saved, participantIds, messageIds, lastMessageAt);
    }

    @Override
    public void updateChannel(UUID channelId, ChannelUpdateRequestDto request) {
        if (channelId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        Channel channelById = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));
        if (channelById.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("해당 채널은 수정할수 없습니다.");
        }
        ChannelUpdateParams params = ChannelUpdateParams.from(request);
        channelById.update(params); // TODO: api ChannelDto 형식으로, 영속성 컨텍스트로 불필요 flag 제거 수정할 것

        channelRepository.save(channelById);

    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);
        // 채널삭제
        channelRepository.deleteById(channel.getId()); // NOTE message, read_status CASCADE 처리됨
        // TODO: 보상로직 생략, DB 생기면 @Transactional 이부분도 곧 넣기
    }

    @Override
    public ChannelResponseDto getChannel(UUID channelId) {
        Channel channel = channelReader.findChannelOrThrow(channelId);
        List<UUID> participantIds = readStatusRepository.findByChannelId(channel.getId()).stream()
                .map(readStatus -> readStatus.getUser().getId())
                .collect(Collectors.toList());
        List<UUID> messageIds = messageRepository.findIdsByChannelId(channel.getId());
        Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId())
                .map(Message::getCreatedAt)
                .orElse(null);
        return getChannelResponseDto(channel, participantIds, messageIds, lastMessageAt);
    }

    @Override
    public List<ChannelResponseDto> getAllChannels() {
        List<Channel> channelList = channelRepository.findAll(); //
        return channelList
                .stream()
                .map(channel -> { // TODO: N+1 발생하는 쿼리가 될거같은데 어떻게 최적화해야할지 생각해보기
                    List<UUID> participantIds = readStatusRepository.findByChannelId(channel.getId()).stream()
                            .map(readStatus -> readStatus.getUser().getId())
                            .collect(Collectors.toList());
                    List<UUID> messageIds = messageRepository.findIdsByChannelId(channel.getId());
                    Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId())
                            .map(Message::getCreatedAt)
                            .orElse(null);
                    return getChannelResponseDto(channel, participantIds, messageIds, lastMessageAt);
                })
                .toList();
    }

    @Override
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        return channelRepository.findAllByUserId(userId)
                .stream()
                .map(channel -> {  // TODO: N+1 발생하는 쿼리가 될거같은데 어떻게 최적화해야할지 생각해보기
                    List<UUID> participantIds = readStatusRepository.findByChannelId(channel.getId()).stream()
                            .map(readStatus -> readStatus.getUser().getId())
                            .collect(Collectors.toList());
                    List<UUID> messageIds = messageRepository.findIdsByChannelId(channel.getId());
                    Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId())
                            .map(Message::getCreatedAt)
                            .orElse(null);
                    return getChannelResponseDto(channel, participantIds, messageIds, lastMessageAt);
                })
                .toList();
    }


    private ChannelResponseDto getChannelResponseDto(Channel channel, List<UUID> participantIds, List<UUID> messageIds, Instant lastMessageAt) {
        if (channel.getType() == ChannelType.PUBLIC) {
            return ChannelResponseDto.from(channel, participantIds, messageIds, lastMessageAt);
        } else if (channel.getType() == ChannelType.PRIVATE) {
            return ChannelResponseDto.from(channel, participantIds, messageIds, lastMessageAt);
        } else {
            throw new IllegalArgumentException("unsupported channel type: " + channel.getType());
        }
    }

    @Override
    public void joinChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);

        User user = userReader.findUserOrThrow(userId);
        // 이미 참여했는지 체크 (메서드 없으면 아래 existsBy...를 리포지토리에 추가)
        if (readStatusRepository.existsByUserAndChannel(user, channel)) {
            throw new IllegalStateException("이미 참여한 유저입니다.");
        }

        // 참여 = ReadStatus 한 줄 생성
        ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
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

        ReadStatus readStatus = readStatusRepository.findByUserAndChannel(user, channel)
                .orElseThrow(() -> new IllegalStateException("채널에 참여하지 않은 사용자입니다."));

        readStatusRepository.delete(readStatus);

    }

    @Override
    public List<User> getAllMembers(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }

        Channel channel = channelReader.findChannelOrThrow(channelId);
        List<UUID> userIds = readStatusRepository.findUserIdsByChannelId(channel.getId());
        return userReader.findUsersByIds(userIds);
    }

    @Override
    public List<Channel> getChannelsByUserId(UUID userId) {
        return readStatusRepository.findChannelsByUserId(userId);
    }
}
