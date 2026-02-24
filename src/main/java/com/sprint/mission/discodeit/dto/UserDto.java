package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "사용자 정보 응답 DTO")
public record UserDto(
    @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Schema(description = "계정 생성 시각", example = "2026-01-01T00:00:00Z")
    Instant createdAt,

    @Schema(description = "정보 수정 시각", example = "2026-02-20T14:00:00Z")
    Instant updatedAt,

    @Schema(description = "사용자 이름", example = "달선")
    String username,

    @Schema(description = "마지막 활동 시간", example = "2026-02-20T14:00:00Z")
    Instant lastActiveAt,

    @Schema(description = "이메일 주소", example = "dalsun@naver.com")
    String email,

    @Schema(description = "프로필 이미지(바이너리) ID", example = "3f3fb215-32aa-4281-904c-45b9dd8b96fb")
    UUID profileId,

    @Schema(description = "현재 접속 여부", example = "true")
    Boolean online
) {

}