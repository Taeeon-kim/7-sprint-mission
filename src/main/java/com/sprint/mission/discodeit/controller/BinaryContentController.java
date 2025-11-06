package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping("/binary-contents/{id}")
    public ResponseEntity<BinaryContentResponseDto> getBinaryContent(
            @PathVariable UUID id
    ) {
        BinaryContentResponseDto binaryContent = binaryContentService.getBinaryContent(id);
        return ResponseEntity.ok(binaryContent);
    }

    @RequestMapping(value = "/binary-contents", method = RequestMethod.GET)
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

    @RequestMapping(value = "/binary-contents", method = RequestMethod.POST)
    public ResponseEntity<UUID> createBinaryContent(
            @RequestBody MultipartFile file
    ) {
        BinaryContentUploadCommand fileCommand = BinaryContentUploadCommand.from(file);
        UUID binaryId = binaryContentService.uploadBinaryContent(fileCommand);
        return ResponseEntity.ok(binaryId);
    }
}
