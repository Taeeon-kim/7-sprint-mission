package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;


    @Override
    public UserResponseDto login(String username, String password) {
        List<User> all = userRepository.findAll();
        User findUser = all.stream()
                .filter((user) -> (user.getNickname().equals(username)) && (user.getPassword().equals(password)))
                .findFirst().orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
        return UserResponseDto.builder()
                .id(findUser.getId())
                .nickname(findUser.getNickname())
                .email(findUser.getEmail())
                .profileId(findUser.getProfileId())
                .role(findUser.getRole())
                .phoneNumber(findUser.getPhoneNumber())
                .build();
    }
}
