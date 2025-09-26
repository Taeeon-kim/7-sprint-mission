package com.sprint.mission.discodeit.entity;

public class Message extends BasicEntity{
    private String content;
    private User sender;
    private User receiver;
    private Channel channel; // TODO: Channel과 연관관계 체크할것

    public Message(String content, User sender, User receiver, Channel channel) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.channel = channel;
    }


}
