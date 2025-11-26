package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.websocket.server.PathParam;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    //사용자 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "User 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨"),
            @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함")
    })
    public void create( @ModelAttribute UserCreateRequestDto userCreateRequestDto) {
        userService.createUser(userCreateRequestDto);
    }

    //단건 조회
//    @GetMapping(params = "userId")
//    public UserResponseDto getUser(@RequestParam String userId) {
//        return userService.findById(userId);
//    }

    //전체 조회
    @GetMapping(params = "!userId")
    @Operation(summary = "전체 User 목록 조회", operationId = "findAll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User 목록 조회 성공")
    })
    public List<UserResponseDto>  findAll /*getUsers*/() {
        return userService.findAllUser();
    }

    //사용자 수정
    @PatchMapping(path = "{userId}", consumes = "multipart/form-data")
    @Operation(summary = "User 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨"),
            @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함"),
            @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
    })
    public void update /*updateUser*/(@PathVariable String userId, @ModelAttribute UserUpdateRequestDto userUpdateRequestDto) {
        userService.updateUser(userId, userUpdateRequestDto);
    }

    //사용자 삭제
    @DeleteMapping("/{userId}")
    @Operation(summary = "User 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
            @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
    })
    public void delete /*deleteUser*/(@PathVariable("userId") UUID userId) {
        userService.deleteUser(userId);
    }
}
