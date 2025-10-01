package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.*;

public class Channel extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private final Set<UUID> userIds;
    private final List<UUID> messageIds; // TODO: 추후 DB 및 레포지토리 계층 사용시 효율성을 위해 Message객체가아닌 messageId만 받는것 고려할 것, 채널이 모든 객체 정보를 가질 필요가있는지 먼저 생각
    private final UUID createdByUserId; // NOTE: 작성자는 수정못하게 final, 해당 유저가 지워질걸 고려해서 User가아닌 userId로 받기
    private boolean isPrivate;


    public Channel(String title, String description, UUID createdByUserId, boolean isPrivate) {

        if (title == null || title.isBlank()){
            throw new IllegalArgumentException("title is invalid");
        }
        if (description == null){
            throw new IllegalArgumentException("description is null");
        }
        if (createdByUserId == null){
            throw new IllegalArgumentException("createdByUserId is null");
        }

        this.title = title;
        this.description = description;
        this.createdByUserId = createdByUserId;
        this.isPrivate = isPrivate;
        this.userIds = new HashSet<>();
        this.messageIds = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<UUID> getUserIds() {
        return userIds.stream().toList();
    }

    public void addUser(UUID userId) {
        if(userId == null){
            throw new IllegalArgumentException("유저정보가 잘못 되었습니다.");
        }
        boolean isAdded = userIds.add(userId);
        if  (!isAdded){ // NOTE: 중복체크
            throw new IllegalStateException("이미 참여한 유저입니다.");
        }
    }

    public void removeUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("유저정보가 잘못 되었습니다.");
        }
        boolean isRemoved = userIds.remove(userId);

        if(!isRemoved){
            throw new IllegalStateException("채널에서 유저가 없습니다.");
        }
    }

    public void addMessageId(UUID messageId) {
        if (messageId == null){
            throw new IllegalArgumentException("메세시 정보가 잘못되었습니다.");
        }
        messageIds.add(messageId);

    }

    public boolean isMember(UUID userId){
            return userIds.contains(userId);
    }

    public List<UUID> getMessageIds() {
        return messageIds.stream().toList();
    }

    public UUID getCreatedByUserId() {
        return createdByUserId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }


    // TODO: Users 에대한 업데이트는 좀더 고려할것 일반적으로 값만 대입하는게 아닌 List로 User를 지우는지,추가하는지 에대한 내용
    // 만약 User내용자체가 바뀌었다면 Channel에서 고려할사항은 아님 어차피 주소값이 있기때문에 알아서 바뀐 User로 될거니깐


    // TODO: updatePrivaqte 변경

    public boolean updateTitle(String title) {
        if (title != null && !title.isBlank() && !title.equals(this.title)) {
            this.title = title;
            return true;
        }
        return false;
    }


    public boolean updateDescription(String description) {
        if (description != null && !description.equals(this.description)) { // NOTE: 설명란은 blank 가능하도록하기위해 isBlank는 체크는 제거
            // NOTE: trim 필요? 그게 설명일수도있는지(부가기능, 만약 내용은 같은데 여백같은거라면 trim하면 같아지는것도 고려)
            this.description = description;
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Channel{" +
                "id='" + getId() + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userIds=" + userIds +
                ", messages=" + messageIds +
                ", createdByUserId=" + createdByUserId +
                ", isPrivate=" + isPrivate +
                '}';
    }

}
