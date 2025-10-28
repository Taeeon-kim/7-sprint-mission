package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.userStatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
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
    public UUID createUserStatus(UserStatusRequestDto request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        User user = userReader.findUserOrThrow(request.getUserId());
        if (userStatusRepository.existsByUserId(user.getId())) {
            throw new IllegalArgumentException("해당 유저의 상태는 이미 등록되었습니다.");
        }

        UserStatus userStatus = new UserStatus(user.getId());
        UserStatus savedStatus = userStatusRepository.save(userStatus);
        return savedStatus.getId();
    }

    @Override
    public UserStatus getUserStatusById(UUID id) {
        return userStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
    }
}
