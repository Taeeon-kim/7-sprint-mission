package com.sprint.mission.discodeit.service.reader;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class MessageReader {
    private final MessageRepository messageRepository;

    public MessageReader(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message findMessageOrThrow(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        Optional<Message> messageById = messageRepository.findById(messageId);
        return messageById.orElseThrow(() -> new NoSuchElementException("메세지가 없습니다"));
    }
}
