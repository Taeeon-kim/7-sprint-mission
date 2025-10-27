package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class BasicUserStatusService implements UserStatusService {


    private final UserReader userReader;
    private final UserStatusRepository userStatusRepository;

    public BasicUserStatusService(UserReader userReader, UserStatusRepository userStatusRepository) {
        this.userReader = userReader;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UUID createUserStatus(UUID userId) {
        if(userId == null){
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        User user = userReader.findUserOrThrow(userId);

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        UserStatus savedStatus = userStatusRepository.save(userStatus);
        return savedStatus.getUserId();
    }
}
