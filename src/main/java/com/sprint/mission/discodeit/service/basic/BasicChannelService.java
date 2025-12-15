package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateParams;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelMinimumMembersNotMetException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.factory.ChannelFactory;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private static final int MIN_PARTICIPANTS_FOR_PRIVATE_CHANNEL = 2;

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelFactory channelFactory;
    private final ChannelMapper channelMapper;

    @Override
    @Transactional
    public ChannelResponseDto createChannel(ChannelCreateCommand command) {

        log.info("채널 생성 시도 type={} title={} members={}",
                command.type(), command.title(), command.memberIds().size()); // NOTE: description은 길어질수있어 생략

        Channel channel = channelFactory.create(command);

        Channel saved = channelRepository.save(channel);

        if (saved.getType() == ChannelType.PRIVATE) {
            List<User> users = userReader.findUsersByIds(command.memberIds());
            if (users.size() < MIN_PARTICIPANTS_FOR_PRIVATE_CHANNEL) {
                log.warn("PRIVATE 채널 생성 실패 - 최소 인원 부족 channelId={} required={} actual={}",
                        saved.getId(), MIN_PARTICIPANTS_FOR_PRIVATE_CHANNEL, users.size());
                throw new ChannelMinimumMembersNotMetException(
                        users.size(),
                        MIN_PARTICIPANTS_FOR_PRIVATE_CHANNEL,
                        users.stream().map(User::getId).toList()
                );
            }
            if (users.size() < command.memberIds().size()) {
                log.warn("PRIVATE 채널 생성 실패 - 잘못된 참여 유저 존재 channelId={} requestedIds={} foundUsers={}",
                        saved.getId(), command.memberIds().size(), users.size());
                throw new IllegalArgumentException("참여 유저가 잘못되었습니다.");
            }

            List<ReadStatus> readStatuses = users.stream()
                    .map(user -> new ReadStatus(user, saved)).toList();

            readStatusRepository.saveAll(readStatuses);
            log.debug("PRIVATE 채널 ReadStatus 생성 channelId={} count={}", saved.getId(), readStatuses.size());
        }
        log.info("채널 생성 성공 channelId={} type={} name={}",
                saved.getId(), saved.getType(), saved.getName());
        return channelMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void updateChannel(UUID channelId, ChannelUpdateRequestDto request) {
        if (channelId == null) { // TODO: 추후 컨트롤러 생성시 책임을 컨트롤러로 넘기고 트레이드오프로 신뢰한다는 가정하에 진행 , 굳이 방어적코드 x
            log.warn("채널 수정 실패 - channelId null");
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        log.info("채널 수정 시도 channelId={}", channelId);
        Channel channelById = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));
        if (channelById.getType() == ChannelType.PRIVATE) {
            log.warn("채널 수정 실패 - PRIVATE 채널 수정 불가 channelId={}", channelId);
            throw new IllegalArgumentException("해당 채널은 수정할수 없습니다.");
        }
        ChannelUpdateParams params = ChannelUpdateParams.from(request);
        channelById.update(params); // TODO: api ChannelDto 형식으로

        channelRepository.save(channelById);
        log.info("채널 수정 성공 channelId={}", channelId);
    }

    @Override
    @Transactional
    public void deleteChannel(UUID channelId) {
        if (channelId == null) {
            log.warn("채널 삭제 실패 - channelId null");
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        log.info("채널 삭제 시도 channelId={}", channelId);

        Channel channel = channelReader.findChannelOrThrow(channelId);

        messageRepository.deleteByChannelId(channel.getId());

        readStatusRepository.deleteByChannelId(channel.getId());

        log.debug("채널 관련 데이터 삭제 channelId={}",
                channel.getId());

        channelRepository.deleteById(channel.getId());
        log.info("채널 삭제 성공 channelId={}", channelId);

        // 채널삭제
        channelRepository.deleteById(channel.getId()); // NOTE : Channel쪽에 연관관계가없어서 CASCADE 발생이안됨 따라서 위에 명시적으로 삭제
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelResponseDto getChannel(UUID channelId) {
        log.debug("채널 단건 조회 시도 channelId={}", channelId);
        Channel channel = channelReader.findChannelOrThrow(channelId);
        return getChannelResponseDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> getAllChannels() {
        log.debug("전체 채널 목록 조회 시도");
        List<Channel> channelList = channelRepository.findAll();
        // TODO: N+1 발생하는 쿼리가 될거같은데 어떻게 최적화해야할지 생각해보기
        // 1. default batch 설정으로 N호출을 낮춤
        // 2. join 문 추가 (미정)
        return channelList
                .stream()
                .map(this::getChannelResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        log.debug("사용자 기준 채널 목록 조회 시도 userId={}", userId);
        return channelRepository.findAllVisibleByUserId(userId)
                .stream()
                .map(this::getChannelResponseDto)
                .toList();
    }

    private ChannelResponseDto getChannelResponseDto(Channel channel) {
        if (channel.getType() == ChannelType.PUBLIC) {
            return channelMapper.toDto(channel);
        } else if (channel.getType() == ChannelType.PRIVATE) {
            return channelMapper.toDto(channel);
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
        log.info("채널 참여 시도 channelId={} userId={}", channelId, userId);
        Channel channel = channelReader.findChannelOrThrow(channelId);

        User user = userReader.findUserOrThrow(userId);
        // 이미 참여했는지 체크 (메서드 없으면 아래 existsBy...를 리포지토리에 추가)
        if (readStatusRepository.existsByUserAndChannel(user, channel)) {
            throw new IllegalStateException("이미 참여한 유저입니다.");
        }

        // 참여 = ReadStatus 한 줄 생성
        ReadStatus readStatus = new ReadStatus(user, channel);
        readStatusRepository.save(readStatus);
        log.info("채널 참여 성공 channelId={} userId={}", channelId, userId);
    }

    @Override
    @Transactional
    public void leaveChannel(UUID channelId, UUID userId) {
        if (channelId == null || userId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }
        log.info("채널 탈퇴 시도 channelId={} userId={}", channelId, userId);
        Channel channel = channelReader.findChannelOrThrow(channelId);

        User user = userReader.findUserOrThrow(userId);

        ReadStatus readStatus = readStatusRepository.findByUserAndChannel(user, channel)
                .orElseThrow(() -> new IllegalStateException("채널에 참여하지 않은 사용자입니다."));
        readStatusRepository.delete(readStatus);
        log.info("채널 탈퇴 성공 channelId={} userId={}", channelId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllMembers(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("전달값을 확인해주세요.");
        }

        log.debug("채널 멤버 조회 시도 channelId={}", channelId);
        Channel channel = channelReader.findChannelOrThrow(channelId);
        List<UUID> userIds = readStatusRepository.findUserIdsByChannelId(channel.getId());
        List<User> users = userReader.findUsersByIds(userIds);
        log.debug("채널 멤버 조회 성공 channelId={} memberCount={}", channelId, users.size());
        return users;
    }

}
