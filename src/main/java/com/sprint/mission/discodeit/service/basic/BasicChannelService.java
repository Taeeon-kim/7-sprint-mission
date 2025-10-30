package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelListResponseDto;
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
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    //Public 패널 생성
    @Override
    public ChannelResponseDto createPublicChannel(ChannelCreateRequestDto channelCreateRequestDto) {
        if(!channelCreateRequestDto.getChannelType().equals(PUBLIC))
            throw new IllegalArgumentException("Public channels only support PUBLIC");
        if(channelCreateRequestDto.getChannelName() == null || channelCreateRequestDto.getChannelName().isBlank()){
            throw new IllegalArgumentException("채널 이름이 필요합니다.");
        }

        Channel ch = new Channel(PUBLIC,  channelCreateRequestDto.getChannelName());
        channelRepository.save(ch);
        return new ChannelResponseDto(
                ch.getUuid(),
                ch.getChanName(),
                ch.getType(),
                ch.getCreateAt(),
                ch.getUpdatedAt(),
                null,
                null
        );
    }

    // PRIVATE 채널 생성
    @Override
    public ChannelResponseDto createPrivateChannel(ChannelCreateRequestDto channelCreateRequestDto) {
        if(channelCreateRequestDto.getChannelType().equals(PRIVATE))
            throw new IllegalArgumentException("Private channels only support PRIVATE");

        List<UUID> participants = channelCreateRequestDto.getParticipantIds();
        if(participants == null || participants.isEmpty()){
            throw new IllegalArgumentException("PRIVATE 참여자가 없습니다.");
        }

        Channel ch = new Channel(PRIVATE, "");
        ch.setParticipantIds(participants);
        channelRepository.save(ch);

        for(UUID userId : participants){
            User u = userRepository.findById(userId);
            if(u != null){
                ReadStatus rs = new ReadStatus(userId, ch.getUuid());
                readStatusRepository.save(rs);
            }
        }

        return new ChannelResponseDto(
                ch.getUuid(),
                ch.getChanName(),
                ch.getType(),
                ch.getCreateAt(),
                ch.getUpdatedAt(),
                null,
                participants
        );
    }


    @Override
    public ChannelResponseDto findChannel(UUID channelId) {
        Channel ch = channelRepository.findByChannel(channelId);
        if(ch == null) return null;

        List<Message> msgs = messageRepository.findChannelAll(ch);
        Instant last = msgs.stream().map(Message->Message.getCreateAt())
                .max(Comparator.naturalOrder()).orElse(null);

        List<UUID> participants = ch.getType() == ChannelType.PRIVATE ? ch.getParticipantIds() : null;

        return new ChannelResponseDto(
                ch.getUuid(),
                ch.getChanName(),
                ch.getType(),
                ch.getCreateAt(),
                ch.getUpdatedAt(),
                last,
                participants
        );
    }

    //특정 사용자 기준 채널 목록
    @Override
    public ChannelListResponseDto findAllByUserId(UUID userId) {
        List<Channel> all = channelRepository.findAll();
        Set<UUID> privateChannels = readStatusRepository.findByUserId(userId)
                .stream().map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());

        List<ChannelResponseDto> results = new ArrayList<>();
        for(Channel ch : all){
            if(ch.getType() == ChannelType.PUBLIC || privateChannels.contains(ch.getUuid())){
                List<Message> msgs = messageRepository.findChannelAll(ch);
                Instant last = msgs.stream().map(Message::getCreateAt)
                        .max(Comparator.naturalOrder()).orElse(null);
                List<UUID> participants = ch.getType() == ChannelType.PRIVATE ? ch.getParticipantIds() : null;
                results.add(new ChannelResponseDto(
                   ch.getUuid(),
                   ch.getChanName(),
                   ch.getType(),
                   ch.getCreateAt(),
                   ch.getUpdatedAt(),
                   last,
                   participants
                ));
            }
        }
        return new ChannelListResponseDto(results);
    }

    // PRIVATE 채널은 Update X
    @Override
    public void updateChannel(ChannelUpdateRequestDto channelUpdateRequestDto) {
        Channel ch = channelRepository.findByChannel(channelUpdateRequestDto.getChannelId());
        if(ch == null) return;
        if(ch.getType() == ChannelType.PRIVATE){
            System.out.println("수정 불가 채널");
            return;
        }
        channelRepository.updateChannel(ch.getUuid(), channelUpdateRequestDto.getNewChannelName());
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel ch = channelRepository.findByChannel(channelId);
        if(ch == null) return;

        //메시지 삭제
        List<Message> msgs = messageRepository.findChannelAll(ch);
        for(Message msg : msgs) messageRepository.deleteMessage(msg.getUuid());

        //ReadStatus 삭제
        readStatusRepository.deleteAllByChannelId(channelId);
        channelRepository.deleteChannel(channelId);
    }

    public List<Channel> getReadChannel(){
        return channelRepository.findAll();
    }

    public void runChannelService() {
        // 채널 생성
        ChannelCreateRequestDto[] channelCreateRequestDtos = {
//                new ChannelCreateRequestDto(PUBLIC, "이벤트"),
//                new ChannelCreateRequestDto(PUBLIC, "공지"),
//                new ChannelCreateRequestDto(PUBLIC, "자유게시판"),
//                new ChannelCreateRequestDto(PRIVATE, "", null, userRepository.findById),
        };
        for (ChannelCreateRequestDto channelCreateResponseDto : channelCreateRequestDtos) {
            createPublicChannel(channelCreateResponseDto);
        }

        // 채널 조회
//        System.out.println("[채널 검색] : " + readChannel(channels[0].getUuid()));

        // 채널명 수정
//        updateChannel(channels[1].getUuid(), "Q & A");
//        updateChannel(channels[0].getUuid(), "공지 및 이벤트");

        // 채널 전체 조회
        channelList();

        // 채널 삭제
//        deleteChannel(channels[1].getUuid());
        // 채널 전체 조회
        channelList();

//        return channels;
    }

    //채널 전체 조회
    public void channelList() {
        System.out.println("[채널 전체 조회]");
        Set<String> channelSet = new HashSet<>();
        for (Channel c : channelRepository.findAll()) {
            if (channelSet.add(c.getChanName())) {
                System.out.println("채널명: " + c.getChanName());
            }
        }
    }

}
