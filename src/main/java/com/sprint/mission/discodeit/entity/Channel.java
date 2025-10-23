package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    //채널 id, 생성, 수정은 BaseEntity에

    private String chanName; //채널 이름String

    public Channel(String chanName /*, List<Message> messages*/) {
        super();
        this.chanName = chanName;
    }

    public void setChanName(String chanName) {
        this.chanName = chanName;
        setUpdatedAt(System.currentTimeMillis()); //채널 이름 변경 시
    }

    public String getChanName() {
        return chanName;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "chanName='" + chanName + '\'' +
                ", createAt=" + getCreateAt() +
                ", updateAt=" + getUpdatedAt() +
                '}';
    }
}
