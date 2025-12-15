package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserDuplicateException extends DiscodeitException {
    public UserDuplicateException(){
        super(ErrorCode.DUPLICATE_USER);
    }
}
