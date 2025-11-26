package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BinaryContentResponseDto> getBinaryContent(
            @PathVariable UUID id
    ) {
        BinaryContentResponseDto binaryContent = binaryContentService.getBinaryContent(id);
        return ResponseEntity.ok(binaryContent);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContentsByIds(
            @RequestParam(required = false) List<UUID> ids
    ) {
        List<BinaryContentResponseDto> binaryContents;
        if (ids == null || ids.isEmpty()) {
            binaryContents = binaryContentService.getAllBinaryContents();
        } else {
            binaryContents = binaryContentService.getBinaryContentsByIds(ids);
        }
        return ResponseEntity.ok(binaryContents);
    }

    @Override
    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> downloadBinaryContent(@PathVariable("binaryContentId")  UUID id) {
        BinaryContentResponseDto responseDto = binaryContentService.getBinaryContent(id);
        return binaryContentStorage.download(responseDto);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UUID> createBinaryContent(
            @RequestPart MultipartFile file
    ) {
        BinaryContentUploadCommand fileCommand = BinaryContentUploadCommand.from(file);
        UUID binaryId = binaryContentService.uploadBinaryContent(fileCommand);
        return ResponseEntity.ok(binaryId);
    }
}
