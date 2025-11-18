package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record MessageSendCommand(
        UUID channelId,
        UUID senderId,
        String content,
        List<BinaryContentUploadCommand> profiles
) {
    public static MessageSendCommand from(MessageSendRequestDto requestDto, List<MultipartFile> imageFiles) {
        List<BinaryContentUploadCommand> attachments = imageFiles == null ? List.of()
                : imageFiles.stream()
                .filter(file -> !file.isEmpty())
                .map(BinaryContentUploadCommand::from)
                .toList();
        return new MessageSendCommand(
                requestDto.channelId(),
                requestDto.authorId(),
                requestDto.content(),
                attachments
        );
    }
}
