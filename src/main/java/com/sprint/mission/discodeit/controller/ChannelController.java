package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
import com.sprint.mission.discodeit.service.ChannelService;
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
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  // 공개 채널 생성
  @Operation(summary = "공개 채널 생성", description = "누구나 참여할 수 있는 공개 채널을 신규 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
  })
  @RequestMapping(value = "/public", method = RequestMethod.POST)
  public ChannelDto createPublicChannel(@RequestBody PublicChannelCreateDto dto) {
    return channelService.createPublic(dto);
  }

  // 비공개 채널 생성
  @Operation(summary = "비공개 채널 생성", description = "초대된 멤버만 참여할 수 있는 비공개 채널을 신규 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelDto.class)))
  })
  @RequestMapping(value = "/private", method = RequestMethod.POST)
  public ChannelDto createPrivateChannel(@RequestBody PrivateChannelCreateDto dto) {
    return channelService.createPrivate(dto);
  }

  // 공개 채널 정보 수정
  @Operation(summary = "채널 정보 수정", description = "기존 채널의 이름이나 설정을 변경합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelDto.class))),
      @ApiResponse(responseCode = "404", description = "채널을 찾을 수 없음")
  })
  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
  public ChannelDto updatePublicChannel(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateDto dto) {
    return channelService.update(channelId, dto);
  }

  // 채널에 멤버 추가
  @Operation(summary = "채널 참여 (멤버 추가)", description = "특정 사용자를 해당 채널의 멤버로 추가합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "참여 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelDto.class)))
  })
  @RequestMapping(value = "/{channel-id}/join", method = RequestMethod.POST)
  public ChannelDto joinChannel(@PathVariable("channel-id") UUID channelId,
      @RequestParam UUID userId) {
    return channelService.joinChannel(userId, channelId);

  }

  // 채널 삭제
  @Operation(summary = "채널 삭제", description = "ID에 해당하는 채널을 영구 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공 (반환 값 없음)"),
      @ApiResponse(responseCode = "404", description = "채널 찾을 수 없음")
  })
  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public void delete(@PathVariable UUID channelId) {

    channelService.delete(channelId);
  }

  // 모든 채널 목록 조회
  @Operation(summary = "사용자 참여 채널 목록 조회", description = "특정 사용자가 참여 중인 모든 채널 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class))
          ))
  })
  @RequestMapping(method = RequestMethod.GET)
  public List<ChannelDto> findAll(@RequestParam UUID userId) {
    return channelService.findAllByUserId(userId);
  }
}
