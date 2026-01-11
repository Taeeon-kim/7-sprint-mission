package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public abstract class AuthException extends DiscodeitException {
    protected AuthException(ErrorCode errorCode){
        super(errorCode);
    }
}
