package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public record UserSignupCommand(
        String username,
        String email,
        String password,
        Optional<BinaryContentUploadCommand> profile
) {

    public static UserSignupCommand from(UserSignupRequestDto requestDto, MultipartFile profileFile) {
        Optional<BinaryContentUploadCommand> profile = Optional.ofNullable(profileFile)
                .filter(file -> !file.isEmpty())
                .map(BinaryContentUploadCommand::from);
        return new UserSignupCommand(
                requestDto.getUsername(),
                requestDto.getEmail(),
                requestDto.getPassword(),
                profile
        );

    }

}
