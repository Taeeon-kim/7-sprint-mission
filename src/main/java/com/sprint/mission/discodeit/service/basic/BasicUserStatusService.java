package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.userStatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundByUserIdException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {


    private final UserReader userReader;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;


    @Override
//    @Transactional
    public UUID createUserStatus(UserStatusRequestDto request) {
        if (request.getUserId() == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        User user = userReader.findUserOrThrow(request.getUserId());
        if (userStatusRepository.existsByUserId(user.getId())) {
            throw new UserStatusAlreadyExistsException(user.getId());
        }

        UserStatus userStatus = new UserStatus(user);
        UserStatus savedStatus = userStatusRepository.save(userStatus);

        log.info("유저 상태 생성 완료 - userStatusId={}, userId={}", savedStatus.getId(), user.getId());

        return savedStatus.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatusResponseDto getUserStatus(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(() -> new UserStatusNotFoundException(id));
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatusResponseDto> getAllUserStatuses() {
        List<UserStatus> userStatusList = userStatusRepository.findAll();
        return userStatusList.stream()
                .map(userStatus -> userStatusMapper.toDto(userStatus))
                .toList();
    }

    @Override
    @Transactional
    public void updateUserStatus(UUID id, UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        if (id == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        log.debug(
                "유저 활동시각 갱신 시도 - userStatusId={}, newLastActiveAt={}",
                id,
                userStatusUpdateRequestDto.newLastActiveAt()
        );

        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(() -> new UserStatusNotFoundException(id));
        boolean isUpdated = userStatus.updateLastActiveAt(userStatusUpdateRequestDto.newLastActiveAt());

        if (isUpdated) {
            userStatusRepository.save(userStatus);
            log.debug("유저 활동시각 갱신 완료 - userStatusId={}", id);
        }
    }

    @Override
    @Transactional
    public void updateUserStatusByUserId(UUID userId, UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        if (userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }

        log.debug("유저 활동시각 갱신 시도 - userId={}, newLastActiveAt={}", userId, userStatusUpdateRequestDto.newLastActiveAt());

        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new UserStatusNotFoundByUserIdException(userId));
        boolean isUpdated = userStatus.updateLastActiveAt(userStatusUpdateRequestDto.newLastActiveAt());
        if (isUpdated) {
            userStatusRepository.save(userStatus);
            log.debug("유저 활동시각 갱신 완료 - userId={}", userId);
        }
    }

    @Override
    @Transactional
    public void deleteUserStatus(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        userStatusRepository.deleteById(id);
        log.info("유저 상태 삭제 완료 - userStatusId={}", id);
    }
}

