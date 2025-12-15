package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 공통
    INVALID_INPUT("입력값이 잘못되었습니다.", HttpStatus.BAD_REQUEST, "CM-001"),
    NOT_FOUND("리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "CM-002"),
    CONFLICT("이미 존재합니다.", HttpStatus.CONFLICT, "CM-003"),
    INVALID_STATE("요청을 처리할 수 없는 상태입니다.", HttpStatus.CONFLICT, "CM-004"),
    INTERNAL_SERVER_ERROR("", HttpStatus.INTERNAL_SERVER_ERROR, "CM-005"),

    // User
    USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "U-001"),
    DUPLICATE_USER("이미 존재하는 유저 입니다.", HttpStatus.CONFLICT, "U-002"),

    // Channel
    CHANNEL_NOT_FOUND("채널을 찾을수 없습니다.", HttpStatus.NOT_FOUND, "CH-001"),
    //    PRIVATE_CHANNEL_UPDATE("프라이빗 채널 업데이트를 할 수 없습니다.", HttpStatus.BAD_REQUEST, "CH-002"), // TODO: 용도 볼것
    CHANNEL_ACCESS_DENIED("채널 권한이 없습니다.", HttpStatus.FORBIDDEN, "CH-003"),
    CHANNEL_MINIMUM_MEMBERS_NOT_MET("PRIVATE 채널 최소 2명 이상이어야 합니다.", HttpStatus.BAD_REQUEST, "CH-004"),

    // Auth
    INVALID_CREDENTIALS("이름 또는 비밀번호가 잘못되었습니다.", HttpStatus.UNAUTHORIZED, "AU-001"),

    // BinaryContent
    BINARY_CONTENT_NOT_FOUND("해당 파일을 찾을수 없습니다.", HttpStatus.NOT_FOUND, "BC-001"),

    // UserStatus
    USER_STATUS_NOT_FOUND("유저 상태를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR, "US-001");

    private final String message;
    private final HttpStatus status;
    private final String code;

    ErrorCode(String message, HttpStatus status, String code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

}
