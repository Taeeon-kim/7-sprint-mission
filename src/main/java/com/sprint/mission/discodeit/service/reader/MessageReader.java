package com.sprint.mission.discodeit.service.reader;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MessageReader {
    private final MessageRepository messageRepository;

    public MessageReader(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message findMessageOrThrow(UUID messageId) {
        if (messageId == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }
        Optional<Message> messageById = messageRepository.findById(messageId);
        return messageById.orElseThrow(() -> new MessageNotFoundException(messageId));
    }
}
