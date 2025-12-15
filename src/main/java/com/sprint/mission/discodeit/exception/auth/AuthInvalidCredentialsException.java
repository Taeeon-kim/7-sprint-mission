package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class AuthInvalidCredentialsException extends DiscodeitException {
    public AuthInvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS);
    }
}
