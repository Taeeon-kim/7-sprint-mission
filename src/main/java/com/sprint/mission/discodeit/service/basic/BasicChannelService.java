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
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicChannelService implements ChannelService {
    private static final int MIN_PARTICIPANTS_FOR_PRIVATE_CHANNEL = 2;

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
    @Transactional
    public ChannelResponseDto createChannel(ChannelCreateCommand command) {

        Channel channel = channelFactory.create(command);

        Channel saved = channelRepository.save(channel);

        if (saved.getType() == ChannelType.PRIVATE) {
            List<User> users = userReader.findUsersByIds(command.memberIds());
            if (users.size() < MIN_PARTICIPANTS_FOR_PRIVATE_CHANNEL) {
                throw new IllegalArgumentException("PRIVATE 채널 최소 2명 이상이어야 합니다.");
            }
            if (users.size() < command.memberIds().size()) {
                throw new IllegalArgumentException("참여 유저가 잘못되었습니다.");
            }

            List<ReadStatus> readStatuses = users.stream()
                    .map(user -> new ReadStatus(user, saved)).toList();

            readStatusRepository.saveAll(readStatuses);
        }

        List<User> participants = readStatusRepository.findByChannelId(saved.getId()).stream()
                .map(readStatus -> readStatus.getUser())
                .collect(Collectors.toList());

        Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId(), PageRequest.of(0, 1))
                .stream()
                .map(Message::getCreatedAt)
                .findFirst()
                .orElse(null);
        return ChannelResponseDto.from(saved, participants, lastMessageAt);
    }

    @Override
    @Transactional
    public void updateChannel(UUID channelId, ChannelUpdateRequestDto request) {
        if (channelId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        Channel channelById = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));
        if (channelById.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("해당 채널은 수정할수 없습니다.");
        }
        ChannelUpdateParams params = ChannelUpdateParams.from(request);
        channelById.update(params); // TODO: api ChannelDto 형식으로

        channelRepository.save(channelById);
    }

    @Override
    @Transactional
    public void deleteChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);

        messageRepository.deleteByChannelId(channel.getId());

        readStatusRepository.deleteByChannelId(channel.getId());

        // 채널삭제
        channelRepository.deleteById(channel.getId()); // NOTE : Channel쪽에 연관관계가없어서 CASCADE 발생이안됨 따라서 위에 명시적으로 삭제
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelResponseDto getChannel(UUID channelId) {
        Channel channel = channelReader.findChannelOrThrow(channelId);
        List<User> participants = readStatusRepository.findByChannelId(channel.getId()).stream()
                .map(readStatus -> readStatus.getUser())
                .collect(Collectors.toList());

        Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId(), PageRequest.of(0, 1))
                .stream()
                .map(Message::getCreatedAt)
                .findFirst()
                .orElse(null);
        return getChannelResponseDto(channel, participants, lastMessageAt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> getAllChannels() {
        List<Channel> channelList = channelRepository.findAll(); //
        return channelList
                .stream()
                .map(channel -> { // TODO: N+1 발생하는 쿼리가 될거같은데 어떻게 최적화해야할지 생각해보기
                    List<User> participants = readStatusRepository.findByChannelId(channel.getId()).stream() // TODO: 중복코드 private 메서드로 빼야하는지
                            .map(readStatus -> readStatus.getUser())
                            .collect(Collectors.toList());

                    Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId(), PageRequest.of(0, 1))
                            .stream()
                            .map(Message::getCreatedAt)
                            .findFirst()
                            .orElse(null);
                    return getChannelResponseDto(channel, participants, lastMessageAt);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {

        return channelRepository.findAllVisibleByUserId(userId)
                .stream()
                .map(channel -> {  // TODO: N+1 발생하는 쿼리가 될거같은데 어떻게 최적화해야할지 생각해보기
                    List<User> participants = readStatusRepository.findByChannelId(channel.getId()).stream()
                            .map(readStatus -> readStatus.getUser()) // LAZY 접근
                            .collect(Collectors.toList());
                    List<UUID> messageIds = messageRepository.findIdsByChannelId(channel.getId());
                    Instant lastMessageAt = messageRepository.findLatestByChannelId(channel.getId(), PageRequest.of(0, 1))
                            .stream()
                            .map(Message::getCreatedAt)
                            .findFirst()
                            .orElse(null);
                    return getChannelResponseDto(channel, participants, lastMessageAt);
                })
                .toList();
    }


    private ChannelResponseDto getChannelResponseDto(Channel channel, List<User> participants, Instant lastMessageAt) {
        if (channel.getType() == ChannelType.PUBLIC) {
            return ChannelResponseDto.from(channel, participants, lastMessageAt);
        } else if (channel.getType() == ChannelType.PRIVATE) {
            return ChannelResponseDto.from(channel, participants, lastMessageAt);
        } else {
            throw new IllegalArgumentException("unsupported channel type: " + channel.getType());
        }
    }

    @Override
    @Transactional
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
        ReadStatus readStatus = new ReadStatus(user, channel);
        readStatusRepository.save(readStatus);

    }

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public List<User> getAllMembers(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }

        Channel channel = channelReader.findChannelOrThrow(channelId);
        List<UUID> userIds = readStatusRepository.findUserIdsByChannelId(channel.getId());
        return userReader.findUsersByIds(userIds);
    }

}
