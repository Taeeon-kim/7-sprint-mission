package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.message.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Message API", description = "메세지 관련 API")
public interface MessageApi {

    @Operation(summary = "메세지 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<MessageUpdateResponseDto> updateMessage(UUID messageId, MessageUpdateRequestDto request);

    @Operation(summary = "메세지 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<Void> deleteMessage(UUID messageId);

    @Operation(summary = "메세지 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<MessageResponseDto> sendMessageByChannelId(MessageSendRequestDto request, List<MultipartFile> files);

    @Operation(summary = "채널 메세지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<List<MessageResponseDto>> getAllMessagesByChannelId(UUID channelId);
}
