package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    //의존성 주입
    private final ChannelRepository channelRepository;

    @Override
    public void createChannel(Channel channel) {
        channelRepository.save(channel);
        System.out.println("[Channel 생성] : " + channel);
    }

    @Override
    public Channel readChannel(UUID uuid) {
        return channelRepository.findByChannel(uuid);
    }

    @Override
    public List<Channel> readAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID uuid, String newName) {
        channelRepository.updateChannel(uuid, newName);
    }

    @Override
    public void deleteChannel(UUID uuid) {
        channelRepository.deleteChannel(uuid);
        System.out.println("[채널 삭제]");
    }

    public void runChannelService() {
        // 채널 생성
        Channel[] channels = {
                new Channel(PUBLIC, "이벤트"),
                new Channel(PUBLIC, "공지"),
                new Channel(PUBLIC, "자유게시판")
        };
        for (Channel c : channels) {
            createChannel(c);
        }

        // 채널 조회
        System.out.println("[채널 검색] : " + readChannel(channels[0].getUuid()));

        // 채널명 수정
        updateChannel(channels[1].getUuid(), "Q & A");
        updateChannel(channels[0].getUuid(), "공지 및 이벤트");

        // 채널 전체 조회
        channelList();

        // 채널 삭제
        deleteChannel(channels[1].getUuid());
        // 채널 전체 조회
        channelList();
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
