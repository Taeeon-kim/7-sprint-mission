package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // === 명령 (Command) 영역 ===
    // 채널 생성
    public void createChannel(
            Channel channel
    ); // TODO: 진행시 일반 List형식말고 HashMap 으로 key : UUID, value : List<Channel> 이렇게 -> 이건 인메모리 jcf 부분에서 하면됨 여기랑 별도

    // 채널 수정 -> 어차피 DB없으니 넘겨주는 값보고 같은지 비교후 다르면 해당 부분수정(그래야 "" 이런것도 지운걸로 인식할테니)
    public void updateChannel(
            Channel channel);

    // 채널 삭제
    public void deleteChannel(UUID channelId); // TODO: HashMap이므로 key 조회후 바로삭제 -> 이건 인메모리 jcf 부분에서 하면됨 여기랑 별도

    // 채널 읽기(정보보기위해)
    public Channel getChannel(UUID channelId); // TODO: HashMap이므로 KEY조회 -> 이건 인메모리 jcf 부분에서 하면됨 여기랑 별도
    // 초기 메세지 가져오기 // 어차피 실시간 아니므로 단일 혹은 요청시에 보내는걸로?

    // 채널 입장
    public void joinChannel(UUID channelId, UUID userId);

    // 채널 퇴장
    public void leaveChannel(UUID channelId, UUID userId);

    //=== 조회 (Query) 영역 ===

    // 참여인원조회 getAllMembers
    public List<User> getAllMembers(UUID channelId);

    // 전체 채널목록 불러오기 getAllChannels (전체 체널 리스트)
    public List<Channel> getAllChannels();

    // 특정 유저의 채널 목록 불러오기 getUserChannels(userId)
    public List<Channel> getChannelsByUserId(UUID userId);
}
