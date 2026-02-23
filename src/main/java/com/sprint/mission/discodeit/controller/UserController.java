package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;

  // 사용자 등록
  @Operation(summary = "사용자 등록", description = "새로운 사용자를 시스템에 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "등록 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
  })
  @RequestMapping(method = RequestMethod.POST)
  public UserResponseDto join(@RequestPart("userCreateRequest") UserCreateDto dto,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    // profile 파일 처리
    UUID profileId = null;

    if (profile != null && !profile.isEmpty()) {
      BinaryContentCreateDto binaryDto =
          new BinaryContentCreateDto(profile.getContentType(), profile.getBytes(),
              profile.getSize(), profile.getOriginalFilename());

      BinaryContentResponseDto savedFile = binaryContentService.create(binaryDto);
      profileId = savedFile.id();
    }

    // DTO에 profileId 세팅해서 새로 생성
    UserCreateDto newDto = new UserCreateDto(
        dto.username(),
        dto.email(),
        dto.password(),
        profileId
    );

    return userService.create(newDto);
  }

  // 사용자 정보 수정
  @Operation(summary = "사용자 정보 수정", description = "이름, 프로필 이미지 등 사용자의 기본 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
  })
  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
  public UserResponseDto update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateDto dto,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    UUID profileId = null;

    if (profile != null && !profile.isEmpty()) {
      BinaryContentCreateDto binaryDto =
          new BinaryContentCreateDto(profile.getContentType(), profile.getBytes(),
              profile.getSize(), profile.getOriginalFilename());

      BinaryContentResponseDto saved =
          binaryContentService.create(binaryDto);

      profileId = saved.id();
    }

    UserUpdateDto newDto =
        new UserUpdateDto(dto.newUsername(), profileId);

    return userService.update(userId, newDto);
  }

  // 사용자 삭제
  @Operation(summary = "사용자 삭제", description = "ID에 해당하는 사용자를 영구 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "삭제 성공")
  })
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public void delete(@PathVariable UUID userId) {
    userService.delete(userId);
  }

  // 모든 사용자 조회
  @Operation(summary = "모든 사용자 조회", description = "시스템에 등록된 전체 사용자 목록을 가져옵니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))
          ))
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserResponseDto>> findAll() {
    return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
  }

  // 사용자 온라인 상태 업데이트
  @Operation(
      summary = "사용자 온라인 상태 업데이트",
      description = "사용자의 상태(ONLINE, OFFLINE, DONOTDISTURB 등)를 변경합니다."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "상태 업데이트 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "사용자 상태 정보를 찾을 수 없음")
  })
  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public UserResponseDto updateStatus(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateByUserIdDto dto) {
    return userStatusService.updateByUserId(userId, dto);
  }
}
