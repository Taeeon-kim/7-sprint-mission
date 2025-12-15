package com.sprint.mission.discodeit.integration.fixtures;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.List;

public final class MessageFixture {

    private MessageFixture() {
    }

    /**
     * 첨부파일 1개가 포함된 메시지를 저장
     */
    public static Message sendMessage(
            User user,
            Channel channel,
            List<BinaryContent> attachments,
            MessageRepository messageRepository
    ) {
        return messageRepository.save(defaultMessage(user, channel, attachments));
    }

    public static Message sendMessage(
            String message,
            User user,
            Channel channel,
            List<BinaryContent> attachments,
            MessageRepository messageRepository
    ) {
        return messageRepository.save(defaultMessage(message, user, channel, attachments));
    }

    /**
     * 첨부파일 1개가 포함된 기본 메시지 엔티티 (save는 호출 X)
     */
    public static Message defaultMessage(
            User user,
            Channel channel,
            List<BinaryContent> attachments
    ) {
        return new Message("message1", user, channel, attachments);
    }

    public static Message defaultMessage(
            String message,
            User user,
            Channel channel,
            List<BinaryContent> attachments
    ) {
        return new Message(message, user, channel, attachments);
    }
}