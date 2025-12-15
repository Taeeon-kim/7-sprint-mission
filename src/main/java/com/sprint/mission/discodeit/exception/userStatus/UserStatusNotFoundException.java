package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserStatusNotFoundException extends DiscodeitException {
    public UserStatusNotFoundException() {
        super(ErrorCode.USER_STATUS_NOT_FOUND);
    }
}
