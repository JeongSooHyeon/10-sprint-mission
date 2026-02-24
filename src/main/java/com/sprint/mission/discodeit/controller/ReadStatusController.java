package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  // 특정 채널 메시지 수신 정보 생성
  @Operation(summary = "읽음 상태 생성", description = "특정 채널에 대한 사용자의 메시지 수신/읽음 정보를 초기 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadStatusDto.class)))
  })
  @RequestMapping(method = RequestMethod.POST)
  public ReadStatusDto create(@RequestBody ReadStatusCreateDto dto) {
    return readStatusService.create(dto);
  }

  // 특정 채널 메시지 수신 정보 수정
  @Operation(summary = "읽음 상태 수정", description = "마지막으로 읽은 메시지 ID 등 수신 정보를 업데이트합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadStatusDto.class))),
      @ApiResponse(responseCode = "404", description = "해당 ID의 읽음 상태 정보를 찾을 수 없음")
  })
  @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
  public ReadStatusDto update(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateDto dto) {
    return readStatusService.update(readStatusId, dto);
  }

  // 특정 사용자의 메시지 수신 정보 조회
  @Operation(summary = "사용자별 읽음 상태 목록 조회", description = "특정 사용자가 속한 모든 채널의 읽음 상태 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = ReadStatusDto.class))
          ))
  })
  @RequestMapping(method = RequestMethod.GET)
  public List<ReadStatusDto> findAllByUserId(@RequestParam UUID userId) {
    return readStatusService.findAllByUserId(userId);
  }
}
