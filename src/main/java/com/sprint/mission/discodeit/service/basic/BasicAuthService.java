package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;


    @Override
    public UserResponseDto login(AuthLoginRequestDto request) {
        List<User> all = userRepository.findAll(); // TODO: 전용 레포지토리 만들어서 사용해도됨 findByUserNameAndPassword
        User findUser = all.stream()
                .filter((user) -> (user.getNickname().equals(request.username())) && (user.getPassword().equals(request.password())))
                .findFirst().orElseThrow(() -> new NoSuchElementException("이름 또는 비밀번호를 다시 확인해주세요"));
        // TODO: userState를 null이 아닌 online 값으로 추가
        UserStatus userStatus = userStatusRepository.findByUserId(findUser.getId()).orElseThrow(() -> new NoSuchElementException("유저 상태가없습니다."));
        userStatus.markAsActive();
        UserStatus saved = userStatusRepository.save(userStatus);
        return UserResponseDto.from(findUser, saved.getUserStatus()); // TODO: 추후 컨트롤러 생기면 넘겨주는 속성만 전달하는 LoginResult로 변경
    }
}
