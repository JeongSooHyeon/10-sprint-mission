package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Slf4j
public class MessageController {

  private final MessageService messageService;
  private final BinaryContentService binaryContentService;

  // 메시지 보내기
  @Operation(summary = "메시지 전송", description = "채널 내에 새로운 메시지를 작성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "전송 성공",
          content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = MessageDto.class)))
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> send(
      @RequestPart("messageCreateRequest") @Valid MessageCreateRequest dto,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws IOException {

    log.info("메시지 전송 요청: authorId={}, channelId={}", dto.authorId(), dto.channelId());
    MessageDto result = messageService.create(dto, attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // 메시지 수정
  @Operation(summary = "메시지 수정", description = "기존에 작성한 메시지의 내용을 변경합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDto.class))),
      @ApiResponse(responseCode = "404", description = "메시지를 찾을 수 없음")
  })
  @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
  public ResponseEntity<MessageDto> update(@PathVariable UUID messageId,
      @RequestBody @Valid MessageUpdateRequest dto) {

    log.info("메시지 수정 요청: messageId={}", messageId);
    MessageDto result = messageService.update(messageId, dto);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  // 메시지 삭제
  @Operation(summary = "메시지 삭제", description = "ID에 해당하는 메시지를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공")
  })
  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {

    log.info("메시지 삭제 요청: messageId={}", messageId);
    messageService.delete(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  // 조회
  @Operation(
      summary = "메시지 목록 조회 및 검색",
      description = "채널 ID, 키워드, 사용자 ID 등 파라미터 조건에 따라 메시지 목록을 필터링하여 조회합니다."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = MessageDto.class))
          ))
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<PageResponse<MessageDto>> getMessages(
      @RequestParam UUID channelId,
      @RequestParam(required = false) Instant cursor,
      @PageableDefault(size = 50, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messageService.getMessages(channelId, cursor, pageable));
  }


}
