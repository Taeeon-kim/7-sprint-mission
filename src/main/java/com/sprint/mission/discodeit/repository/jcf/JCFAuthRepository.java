package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.AuthRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JCFAuthRepository implements AuthRepository {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUserIdAndPassword(String userId, String password) {
        for(User user : userRepository.findAll()){
            if(user.getUserId().equals(userId) && user.getPassword().equals(password)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
