package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // === 명령 (Command) 영역 ===
    // 채널 생성
     void createChannel(
            String title, String description, UUID createdByUserId
    ); // TODO: 추후 컨트롤러 계층생성시 파라미터를 DTO로 변경(파라미터가 길어질시)

    // 채널 수정 -> 어차피 DB없으니 넘겨주는 값보고 같은지 비교후 다르면 해당 부분수정(그래야 "" 이런것도 지운걸로 인식할테니)
     void updateChannel(
            UUID channelId, String title, String description); // TODO: 추후 컨트롤러 계층생성시 파라미터를 DTO로 변경(파라미터가 길어질시)

    // 채널 메세지 추가
    void addMessageToChannel(Channel channel, UUID messageId);
    // 채널 삭제
     void deleteChannel(UUID channelId);

    // 채널 읽기(정보보기위해)
     Channel getChannel(UUID channelId);
    // 초기 메세지 가져오기 // 어차피 실시간 아니므로 단일 혹은 요청시에 보내는걸로?

    // 채널 입장
     void joinChannel(UUID channelId, UUID userId);

    // 채널 퇴장
     void leaveChannel(UUID channelId, UUID userId);

    //=== 조회 (Query) 영역 ===

    // 참여인원조회 getAllMembers
     List<User> getAllMembers(UUID channelId);

    // 전체 채널목록 불러오기 getAllChannels (전체 체널 리스트)
     List<Channel> getAllChannels();

    // 특정 유저의 채널 목록 불러오기 getUserChannels(userId)
     List<Channel> getChannelsByUserId(UUID userId);
}
