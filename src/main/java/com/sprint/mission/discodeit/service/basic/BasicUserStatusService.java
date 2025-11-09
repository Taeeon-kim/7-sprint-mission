package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatusResponseDto create(UserStatusCreateRequestDto userStatusCreateRequestDto) {
        User user = userRepository.findById(userStatusCreateRequestDto.getUserId());
        if(user==null){
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
        UserStatus existing = userStatusRepository.findByUserId(userStatusCreateRequestDto.getUserId());
        if(existing!=null){
            throw new RuntimeException("이미 UserStatus가 존재합니다.");
        }
        UserStatus userStatus = new UserStatus(userStatusCreateRequestDto.getUserId());
        userStatusRepository.save(userStatus);
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto findById(UUID uuid) {
        UserStatus userStatus = userStatusRepository.findAll().stream()
                .filter(s->s.getUuid().equals(uuid))
                .findFirst().orElseThrow(()->new RuntimeException("해당 UserStatus를 찾을 수 없습니다."));
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(UserStatusResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatusResponseDto update(UUID uuid, UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        UserStatus userStatus = userStatusRepository.findAll().stream()
                .filter(s->s.getUuid().equals(uuid))
                .findFirst().orElseThrow(()->new RuntimeException("해당 UserStatus를 찾을 수 없습니다."));

        if(userStatusUpdateRequestDto.getLastActiveAt()!=null){
            userStatus.updateLastActiveAt();
        }
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        if(userStatus==null){
            throw new RuntimeException("해당 유저의 상태를 찾을 수 없습니다");
        }
        if(userStatusUpdateRequestDto.getLastActiveAt()!=null){
            userStatus.updateLastActiveAt();
        }
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public void delete(UUID uuid) {
        userStatusRepository.delete(uuid);
    }
}
