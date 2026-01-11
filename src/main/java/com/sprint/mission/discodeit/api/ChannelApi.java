package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
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

@Tag(name = "Channel API", description = "채널 관련 API")
public interface ChannelApi {

    @Operation(summary = "모든 채널 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<List<ChannelResponseDto>> getAllChannels(UUID userId);

    @Operation(summary = "일반 채널 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<ChannelResponseDto> createChannelPublic(ChannelCreateRequestDto request);

    @Operation(summary = "개인 채널 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<ChannelResponseDto> createChannelPrivate(ChannelCreateRequestDto request);

    @Operation(summary = "채널 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<ChannelResponseDto> getChannel(UUID channelId);

    @Operation(summary = "채널 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<Void> updateChannel(UUID channelId, ChannelUpdateRequestDto request);

    @Operation(summary = "채널 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<Void> deleteChannel(UUID channelId);
}
