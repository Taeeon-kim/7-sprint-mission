package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDto(
        @NotBlank String newUserName,
        @NotBlank @Email String newEmail,
        @NotBlank String newPassword
) {
}
