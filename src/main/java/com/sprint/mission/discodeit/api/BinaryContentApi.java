package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentApi {

    @Operation(summary = "바이너리컨텐츠 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<BinaryContentResponseDto> getBinaryContent(UUID id);

    @Operation(summary = "특정 다수 바이너리컨텐츠 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContentsByIds(List<UUID> ids);

    @Operation(summary = "바이너리컨텐츠 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<UUID> createBinaryContent(MultipartFile file);

}
