package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequestDto {
//    private UUID authorId; // userId;
    private User author;
//    private String userName;
//    private UUID channelId;
    private Channel channel;
//    private String channelName;
    private String content;
//    private List<UUID> attachments =  new ArrayList<>();
    private List<BinaryContent> attachmentIds;
}
