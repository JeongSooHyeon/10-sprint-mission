package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  // 바이너리 파일 생성
  @Operation(summary = "파일 업로드", description = "MultipartFile을 업로드하여 시스템에 저장")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "업로드 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = BinaryContentResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")

  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BinaryContentResponseDto> create(@RequestPart("file") MultipartFile file)
      throws IOException {
    String fileName = file.getOriginalFilename();
    Path savePath = Paths.get("./uploads/" + fileName);
    Files.createDirectories(savePath.getParent());
    file.transferTo(savePath);

    BinaryContentCreateDto newDto = new BinaryContentCreateDto(file.getContentType(),
        file.getBytes(), file.getSize(), file.getOriginalFilename());
    return new ResponseEntity<>(binaryContentService.create(newDto), HttpStatus.CREATED);
  }

  // 바아너리 파일 1개 조회
  @Operation(
      summary = "바이너리 파일 단건 조회",
      description = "고유 식별자(UUID)를 사용하여 특정 파일의 타입과 바이트 데이터를 조회합니다."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = BinaryContentResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 파일 ID")
  })
  @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContentResponseDto> findById(
      @PathVariable("binaryContentId") UUID id) {
    return new ResponseEntity<>(binaryContentService.findById(id), HttpStatus.OK);
  }

  // 바이너리 파일 여러개 조회
  @Operation(
      summary = "바이너리 파일 다건 조회",
      description = "여러 개의 UUID 목록을 받아 해당되는 모든 파일 정보를 리스트로 반환합니다."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = BinaryContentResponseDto.class))
          ))
  })
  @RequestMapping(method = RequestMethod.GET)
  public List<BinaryContentResponseDto> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> idList) {
    return binaryContentService.findAllByIdIn(idList);
  }
}
