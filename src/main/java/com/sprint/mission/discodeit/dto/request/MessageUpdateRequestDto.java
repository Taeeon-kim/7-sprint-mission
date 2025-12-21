package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class MessageUpdateRequestDto {
    private Message messageId;
    private String content;
}
