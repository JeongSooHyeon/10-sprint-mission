package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;
  private final BinaryContentService binaryContentService;

  // 메시지 보내기
  @Operation(summary = "메시지 전송", description = "채널 내에 새로운 메시지를 작성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "전송 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDto.class)))
  })
  @RequestMapping(method = RequestMethod.POST)
  public MessageResponseDto send(
      @RequestPart("messageCreateRequest") MessageCreateDto dto,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws IOException {

    List<UUID> attachmentIds = new ArrayList<>();
    if (attachments != null && !attachments.isEmpty()) {
      for (MultipartFile file : attachments) {
        if (!file.isEmpty()) {
          BinaryContentCreateDto binaryDto = new BinaryContentCreateDto(
              file.getContentType(),
              file.getBytes(),
              file.getSize(),
              file.getOriginalFilename()
          );
          // Controller가 직접 BinaryContentService를 호출
          BinaryContentResponseDto savedFile = binaryContentService.create(binaryDto);
          attachmentIds.add(savedFile.id());
        }
      }
    }

    return messageService.create(dto, attachmentIds);
  }

  // 메시지 수정
  @Operation(summary = "메시지 수정", description = "기존에 작성한 메시지의 내용을 변경합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "메시지를 찾을 수 없음")
  })
  @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
  public MessageResponseDto update(@PathVariable UUID messageId,
      @RequestBody MessageUpdateDto dto) {
    return messageService.update(messageId, dto);
  }

  // 메시지 삭제
  @Operation(summary = "메시지 삭제", description = "ID에 해당하는 메시지를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "삭제 성공")
  })
  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public void delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
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
              array = @ArraySchema(schema = @Schema(implementation = MessageResponseDto.class))
          ))
  })
  @RequestMapping(method = RequestMethod.GET)
  public List<MessageResponseDto> getMessages(
      @RequestParam(required = false) UUID channelId,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) UUID userId) {

    // 키워드 검색
    if (keyword != null) {
      return messageService.searchMessage(channelId, keyword);
    }

    // 특정 채널의 메시지 목록 조회
    if (channelId != null) {
      return messageService.findAllByChannelId(channelId);
    }

    // 특정 사용자가 보낸 메시지 조회
    if (userId != null) {
      return messageService.getUserMessages(userId);
    }

    // 모든 조건 없으면 전체 조회
    return new ArrayList<>();
  }


}
