package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
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

    @RequestMapping("/binary-contents")
    public ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContentsByIds(
            @RequestParam List<UUID> ids
    ){
        List<BinaryContentResponseDto> binaryContentsByIds = binaryContentService.getBinaryContentsByIds(ids);
        return ResponseEntity.ok(binaryContentsByIds);
    }

}
