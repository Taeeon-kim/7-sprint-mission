package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;

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
