package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.readStatus.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus API", description = "유저 해당 채널 메세지 읽음 관련 API")
public interface ReadStatusApi {

    @Operation(summary = "유저 상태 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<ReadStatusResponseDto> getReadStatus(UUID readStatusId);

    @Operation(summary = "유저 상태 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<ReadStatusResponseDto> createReadStatus(ReadStatusCreateRequestDto request);

    @Operation(summary = "유저 상태 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<ReadStatusUpdateResponseDto> updateReadStatus(UUID id, ReadStatusUpdateRequestDto requestDto);

    @Operation(summary = "특정 유저 상태 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatusesByUserId(UUID userId);
}
