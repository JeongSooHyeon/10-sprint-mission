package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
    @Schema(description = "상태 정보 고유 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    UUID id,

    @Schema(description = "해당 상태의 사용자 ID", example = "3f3fb215-32aa-4281-904c-45b9dd8b96fb")
    UUID userId,

    @Schema(description = "마지막 활동 시간 (UTC)", example = "2026-03-05T18:10:00Z")
    Instant lastActiveAt
) {

}
