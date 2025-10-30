package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @ToString
public class Channel extends BaseEntity {
    //채널 id, 생성, 수정은 BaseEntity에

    private ChannelType type;
    private String chanName; //채널 이름String

    private List<UUID> participantIds; //PRIVATE 채널 참여자 목록
    private Instant lastMessageAt; //최근 메시지 시간 캐시

    public Channel(ChannelType type, String chanName) {
        super();
        this.type = type;
        this.chanName = chanName;
        this.participantIds = new ArrayList<>();
        this.lastMessageAt = null;
    }

    // PRIVATE 채널 생성 시 사용
    public Channel(ChannelType type, String chanName, List<UUID> participantIds) {
        this(type, chanName);
        if(participantIds != null){
            this.participantIds.addAll(participantIds);
        }
    }

    public void setChanName(String chanName) {
        this.chanName = chanName;
        setUpdatedAt(Instant.now()); //채널 이름 변경 시
    }

    // 채널 참가자 리스트를 외부에서 한 번에 교체함
    public void setParticipantIds(List<UUID> participantIds) {
        if(participantIds != null){
            this.participantIds.addAll(participantIds);
            this.participantIds.addAll(participantIds);
        }
    }

    // 채널에 한 명씩 참가자를 추가할 때
    public void addParticipantId(UUID userId) {
        if(!this.participantIds.contains(userId)){
            this.participantIds.add(userId);
            setUpdatedAt(Instant.now());
        }
    }

    // 특정 사용자 목록에서 제거
    public void removeParticipantId(UUID userId) {
        if(this.participantIds.contains(userId)){
            setUpdatedAt(Instant.now());
        }
    }

    // 최근 메시지 순 정렬
    public void setLastMessageAt(Instant lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
        setUpdatedAt(Instant.now());
    }
}
