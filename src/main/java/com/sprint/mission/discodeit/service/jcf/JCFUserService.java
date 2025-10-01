package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

import static com.sprint.mission.discodeit.entity.RoleType.USER;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }

    @Override
    public void signUp(String nickname, String email, String password, String PhoneNumber) {
            /*
                서비스가 지금 경계(boundary) 이므로, 모든 public 서비스 메서드는 자기 파라미터를 직접 검증(널/형식)하는 걸 권장.
        	•	이후에 내부에서 userService.getUserById(userId)가 또 검증하더라도, 중복을 감수하고 입구에서 한 번 더 명시하는 쪽이 유지보수에 안전함.
        	•	이유: 미래에 리팩토링되면서 getUserById 호출이 빠질 수도 있고, 다른 경로에서 이 메서드를 재사용할 수도 있음. 그때 입구 가드가 없으면 취약해짐
            •	성능 오버헤드는 사실상 무시해도 됨(널 체크는 O(1)).
         */
        if (
                nickname == null ||
                        nickname.isBlank() ||
                        password == null ||
                        password.isBlank() ||
                        email == null || email.isBlank() ||
                        PhoneNumber == null ||
                        PhoneNumber.isBlank()
        ) {
            throw new IllegalArgumentException("입력값이 잘못되었습니다.");
        }
        User user = new User(nickname, email, password, USER, PhoneNumber);
        User prev = data.put(user.getId(), user);
        if (prev != null) {
            throw new IllegalStateException("이미 존재하는 사용자 입니다.: " + user.getId());
        }
    }

    @Override
    public User getUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return Optional.ofNullable(data.get(userId)).orElseThrow(() -> new NoSuchElementException("사용자가 없습니다"));
    }

    @Override
    public void deleteUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        data.remove(userId);
    }

    @Override
    public void updateUser(UUID userId, String nickname, String email, String password, String phoneNumber) {
        if(userId == null){ // NOTE: update 는 부분 변경이므로 userId만 가드, 나머지는 Null 허용으로 미변경 정책으로 봄
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        User userById = null;
        try {
            userById = getUserById(userId);
            boolean changeFlag = false;
            changeFlag |= userById.updateNickname(nickname);
            changeFlag |= userById.updateEmail(email);
            changeFlag |= userById.updatePassword(password);
            changeFlag |= userById.updatePhoneNumber(phoneNumber);
            if (changeFlag) {
                userById.setUpdatedAt(System.currentTimeMillis());
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        return data.values()
                .stream()
                .sorted(Comparator.comparing(User::getCreatedAt))
                .toList();
    }

    @Override
    public List<User> getUsersByIds(List<UUID> userIds) {
        if(userIds == null){
            throw new IllegalArgumentException("입력값이 잘못 되었습니다.");
        }
        return userIds.stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .toList();
    }


}
