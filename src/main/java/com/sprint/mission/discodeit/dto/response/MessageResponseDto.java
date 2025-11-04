package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
    private UUID messageId;
    private UUID senderId;
    private String senderNickName;
    private UUID channelId;
    private String channelName;
    private String content;
    private List<UUID> attachmentIds;
    private Instant createAt;
    private Instant updateAt;
}
