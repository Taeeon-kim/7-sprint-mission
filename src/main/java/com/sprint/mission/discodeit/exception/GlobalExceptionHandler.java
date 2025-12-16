package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn(e.getMessage());
        ErrorCode code = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(code.getStatus())
                .body(new ErrorResponse(
                        Instant.now(),
                        code.getCode(),
                        code.getMessage(),
                        Map.of("reason", e.getMessage()),
                        e.getClass().getSimpleName(),
                        code.getStatus().value()
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage());
        ErrorCode code = ErrorCode.INVALID_STATE;
        return ResponseEntity.status(code.getStatus())
                .body(new ErrorResponse(
                        Instant.now(),
                        code.getCode(),
                        code.getMessage(),
                        Map.of("reason", e.getMessage()),
                        e.getClass().getSimpleName(),
                        code.getStatus().value()
                ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());
        ErrorCode code = ErrorCode.NOT_FOUND;
        return ResponseEntity.status(code.getStatus())
                .body(new ErrorResponse(
                        Instant.now(),
                        code.getCode(),
                        code.getMessage(),
                        Map.of("reason", e.getMessage()),
                        e.getClass().getSimpleName(),
                        code.getStatus().value()
                ));
    }

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        log.error(e.getMessage());
        ErrorCode code = e.getErrorCode();
        return ResponseEntity.status(code.getStatus())
                .body(new ErrorResponse(
                        e.getTimestamp(),
                        code.getCode(),
                        code.getMessage(),
                        e.getDetails(),
                        e.getClass().getSimpleName(),
                        code.getStatus().value()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {

        // 필드별 에러를 detail로 내려주고 싶으면 이렇게
        Map<String, Object> detail = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a, // 같은 필드 중복시 첫 값 유지
                        LinkedHashMap::new
                ));

        DiscodeitException ex = new DiscodeitException(ErrorCode.INVALID_INPUT, detail);
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(
                        Instant.now(),
                        errorCode.getCode(),
                        errorCode.getMessage(),
                        ex.getDetails(),
                        ex.getClass().getSimpleName(),
                        errorCode.getStatus().value()

                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());
        ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                Instant.now(),
                                code.getCode(),
                                e.getMessage(),
                                Map.of(),
                                e.getClass().getSimpleName(),
                                code.getStatus().value()
                        )
                );
    }
}
