package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //사용자 생성
//    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createUser( @ModelAttribute UserCreateRequestDto userCreateRequestDto) {
        userService.createUser(userCreateRequestDto);
    }

    //단건 조회
//    @RequestMapping(method = RequestMethod.GET, params = "userId")
    @GetMapping(params = "userId")
    public UserResponseDto getUser(@RequestParam String userId) {
        return userService.findById(userId);
    }

    //전체 조회
//    @RequestMapping(method = RequestMethod.GET, params = "!userId")
    @GetMapping(params = "!userId")
    public List<UserResponseDto> getUsers() {
        return userService.findAllUser();
    }

    //사용자 수정
//    @RequestMapping(method = RequestMethod.PUT, params = "userId", consumes = "multipart/form-data")
    @PatchMapping("/{userId}")
    public void updateUser(@PathVariable String userId, @ModelAttribute UserUpdateRequestDto userUpdateRequestDto) {
        userService.updateUser(userId, userUpdateRequestDto);
    }

    //사용자 삭제
//    @RequestMapping(method = RequestMethod.DELETE, params = "uuid")
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
    }
}
