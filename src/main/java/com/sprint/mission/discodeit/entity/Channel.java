package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends BaseEntity {
    //채널 id, 생성, 수정은 BaseEntity에

    private String chanName; //채널 이름String
    private final List<Message> messages; //메시지 목록 : List

    public Channel(String chanName /*, List<Message> messages*/) {
        super();
        this.chanName = chanName;
        this.messages = new ArrayList<>(); //messages;
    }

    public void setChanName(String chanName) {
        this.chanName = chanName;
        setUpdatedAt(System.currentTimeMillis()); //채널 이름 변경 시
    }

    public String getChanName() {
        return chanName;
    }

    public List<Message> getMessages() {
        return messages;
    }

    //채널에 메시지 추가
    public void addMessage(Message msg){
        messages.add(msg);
        setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Channel{" +
                "chanName='" + chanName + '\'' +
                ", messages=" + messages + //.size()
                ", createAt=" + getCreateAt() +
                ", updateAt=" + getUpdatedAt() +
                '}';
    }
}
