package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.userStatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.springframework.stereotype.Service;

import java.util.List;
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

        UserStatus userStatus = new UserStatus(user);
        UserStatus savedStatus = userStatusRepository.save(userStatus);
        return savedStatus.getId();
    }

    @Override
    public UserStatusResponseDto getUserStatus(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> getAllUserStatuses() {
        List<UserStatus> userStatusList = userStatusRepository.findAll();
        return userStatusList.stream()
                .map(userStatus -> UserStatusResponseDto.from(userStatus))
                .toList();
    }

    @Override
    public void updateUserStatus(UUID id, UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        if (id == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
        boolean isUpdated = userStatus.updateLastActiveAt(userStatusUpdateRequestDto.newLastActiveAt());
        if (isUpdated) {
            userStatusRepository.save(userStatus);
        }

    }

    @Override
    public void updateUserStatusByUserId(UUID userId, UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        if (userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
        boolean isUpdated = userStatus.updateLastActiveAt(userStatusUpdateRequestDto.newLastActiveAt());
        if (isUpdated) {
            userStatusRepository.save(userStatus);
        }
    }

    @Override
    public void deleteUserStatus(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        userStatusRepository.deleteById(id);
    }
}
