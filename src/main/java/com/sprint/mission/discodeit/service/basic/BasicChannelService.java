package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    //의존성 주입
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository; //테스트용

    @Override
    public ChannelResponseDto createPublicChannel(ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {
        if (channelPublicCreateRequestDto.getName() == null
                || channelPublicCreateRequestDto.getName().isBlank()) {
            throw new IllegalStateException("채널 이름이 필요합니다.");
        }

        Channel channel = new Channel(channelPublicCreateRequestDto.getName(), PUBLIC);
        channelRepository.save(channel);

        return ChannelResponseDto.from(channel, null, null);
    }

    @Override
    public ChannelResponseDto createPrivateChannel(ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {
        List<UUID> participantIds = channelPrivateCreateRequestDto.getParticipantIds();
        if (participantIds == null || participantIds.size() <= 1) {
            throw new IllegalStateException("2명 이상의 참여 유저가 있어야 합니다.");
        }
        Channel channel = new Channel(participantIds, PRIVATE);
        channelRepository.save(channel);

        //유저별 ReadStatus 생성
        for (UUID userId : participantIds) {
            ReadStatus readStatus = new ReadStatus(userId, channel.getUuid());
            readStatusRepository.save(readStatus);
//            System.out.println("[ReadStatus] : " + readStatus);
        }
        return ChannelResponseDto.from(channel, null, participantIds);
    }

    @Override
    public ChannelResponseDto findById(UUID channelId) {
        Channel channel = channelRepository.findByChannel(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
        //가장 최근 메시지 시간 정보 포함
        Instant lastMessageAt = messageRepository.findLastByChannel(channelId).orElse(null);

        //PRIVATE의 경우 참여한 user id 포함
        List<UUID> participantIds = null;
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            participantIds = channel.getParticipantIds();
        }

        return ChannelResponseDto.from(channel, lastMessageAt, participantIds);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(c -> c.getChannelType() == PUBLIC
                        || (c.getChannelType() == PRIVATE && c.getParticipantIds().contains(userId)))
                .map(c -> {
                    Instant lastMessageAt = messageRepository.findLastByChannel(c.getUuid()).orElse(null);
                    List<UUID> participantIds = (c.getChannelType() == PRIVATE) ? c.getParticipantIds() : null;
                    return ChannelResponseDto.from(c, lastMessageAt, participantIds);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannel(UUID uuid, ChannelUpdateRequestDto channelUpdateRequestDto) {
        Channel channel = channelRepository.findByChannel(uuid)
                .orElseThrow(() -> new IllegalArgumentException("수정할 채널이 없습니다."));
        if(channel.getChannelType() == PRIVATE){
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        if(channelUpdateRequestDto.getNewName() != null &&
                !channelUpdateRequestDto.getNewName().isBlank()){
            channel.setUpdate(channelUpdateRequestDto.getNewName());
        }

        channel.setUpdatedAt(Instant.now());
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findByChannel(channelId).orElse(null);
        if(channel == null) return;

        List<Message> messages = messageRepository.findAllByChannelId(channel);
        for(Message message : messages){ messageRepository.deleteMessage(message.getUuid());}

        readStatusRepository.delete(channelId);
        channelRepository.deleteChannel(channelId);
    }
}
