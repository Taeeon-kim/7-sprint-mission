package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class AuthInvalidCredentialsException extends AuthException {
    public AuthInvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS);
    }
}
