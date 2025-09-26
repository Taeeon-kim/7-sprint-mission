package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BasicEntity {
    private String title;
    private String description;
    private List<User> users;
    private List<Message> messages;
    private User createdBy;
    private boolean isPrivate;

    public Channel(String title, String description, List<User> users, User createdBy, boolean isPrivate) {
        this.title = title;
        this.description = description;
        this.users = users;
        this.createdBy = createdBy;
        this.isPrivate = isPrivate;
        messages = new ArrayList<>();
    }


}
