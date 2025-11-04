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
    private final ReadStatusRepository readStatusRepository;

    public BasicChannelService(ChannelRepository channelRepository, MessageRepository messageRepository, UserReader userReader, ChannelReader channelReader, UserStatusRepository userStatusRepository, ReadStatusRepository readStatusRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.userReader = userReader;
        this.channelReader = channelReader;
        this.readStatusRepository = readStatusRepository;
    }

    @Override
    public UUID createChannel(ChannelCreateCommand command) {

        User creator = userReader.findUserOrThrow(command.userId());

        Channel channel = switch (command.type()) {
            case PUBLIC -> {
                ChannelCreatePublicParams params = ChannelCreatePublicParams.from(command);
                yield createPublicChannel(creator.getId(), params);
            }
            case PRIVATE -> {
                new ChannelCreatePrivateParams(command.memberIds());
                ChannelCreatePrivateParams params = ChannelCreatePrivateParams.from(command);
                yield createPrivateChannel(creator.getId(), params);
            }
            default -> throw new IllegalArgumentException("unsupported channel type: " + command.type());
        };
        Channel saved = channelRepository.save(channel);
        return saved.getId();
    }

    private Channel createPrivateChannel(UUID createdByUserId, ChannelCreatePrivateParams params) {
        List<UUID> memberIds = params.memberIds();

        if (createdByUserId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        User createdUser = userReader.findUserOrThrow(createdByUserId);
        Channel channel = Channel.createPrivateChannel(createdUser.getId());

        // NOTE: readStatus 생성 로직 부분
        memberIds.forEach((memberId) -> {
            User user = userReader.findUserOrThrow(memberId);
            channel.addUserId(user.getId()); // TODO: 이부분 뭔가 분리해서 넣는게 나을거같은데 시간상 추후에 고민해볼것
            ReadStatus readStatus = new ReadStatus(user.getId(), channel.getId(), Instant.now());
            readStatusRepository.save(readStatus);
        });

        return channel;
    }

    private Channel createPublicChannel(UUID createdByUserId, ChannelCreatePublicParams requestDto) {
        if (createdByUserId == null || requestDto.title() == null || requestDto.title().isBlank() || requestDto.description() == null || requestDto.description().isBlank()) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        User user = userReader.findUserOrThrow(createdByUserId);
        return Channel.createPublicChannel(user.getId(), requestDto.title(), requestDto.description());
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
        boolean changeFlag = channelById.update(params);
        if (changeFlag) {
            channelRepository.save(channelById);
        }
    }


    @Override
    public void deleteChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        Channel channel = channelReader.findChannelOrThrow(channelId);
        // 메세지 레포지토리에서 삭제 로직
        List<UUID> channelMessageIds = channel.getMessageIds();
        for (UUID messageId : channelMessageIds) {
            messageRepository.deleteById(messageId);
        }
        // readStatus 삭제
        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channel.getId());
        for (ReadStatus readStatus : readStatuses) {
            readStatusRepository.deleteById(readStatus.getId());
        }
        // 채널삭제
        channelRepository.deleteById(channel.getId());
        // NOTE: 보상로직 생략, DB 생기면 @Transactional
    }

    @Override
    public ChannelResponseDto getChannel(UUID channelId) {
        return toChannelResponseDto(channelId);
    }

    @Override
    public List<ChannelResponseDto> getAllChannels() {

        List<Channel> channelList = channelRepository.findAll();
        return channelList
                .stream()
                .map(channel -> toChannelResponseDto(channel.getId()))
                .toList();
    }

    @Override
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        return channelRepository.findAllByUserId(userId)
                .stream()
                .map(channel -> toChannelResponseDto(channel.getId()))
                .toList();
    }

    // 헬퍼 메서드
    private ChannelResponseDto toChannelResponseDto(UUID channelId) {
        Channel channel = channelReader.findChannelOrThrow(channelId);
        Instant createdAt = null;
        // 최신 메세지 하나가져오고
        List<UUID> messageIds = channel.getMessageIds();
        // 해당 메세지의 createdAt 추출하고 response dto에 포함
        if (!messageIds.isEmpty()) {
            UUID currentMessageId = messageIds.get(messageIds.size() - 1);

            createdAt = messageRepository.findById(currentMessageId)
                    .map(Message::getCreatedAt)
                    .orElse(null);

        }
        return getChannelResponseDto(channel, createdAt);
    }

    private static ChannelResponseDto getChannelResponseDto(Channel channel, Instant createdAt) {
        if (channel.getType() == ChannelType.PUBLIC) {
            return ChannelResponseDto.from(channel, createdAt);
        } else if (channel.getType() == ChannelType.PRIVATE) {
            return ChannelResponseDto.from(channel, createdAt);
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
        channel.addUserId(user.getId());
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
    public List<Channel> getChannelsByUserId(UUID userId) {
        User userById = userReader.findUserOrThrow(userId);
        List<Channel> allChannels = channelRepository.findAll();
        return allChannels.stream()
                .filter(channel -> channel.isMember(userById.getId()))
                .toList();
    }
}
