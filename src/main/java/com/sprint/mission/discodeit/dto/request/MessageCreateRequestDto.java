package com.sprint.mission.discodeit.dto.request;

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
    private UUID userId;
    private String userName;
    private UUID channelId;
    private String channelName;
    private String content;
    private List<UUID> attachments =  new ArrayList<>();
}
