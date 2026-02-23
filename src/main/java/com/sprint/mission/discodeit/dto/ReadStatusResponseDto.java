package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "채널별 메시지 읽음 상태 정보 DTO")
public record ReadStatusResponseDto(
    @Schema(description = "읽음 상태 기록 ID", example = "7b98f215-32aa-4281-904c-45b9dd8b96fc")
    UUID id,

    @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID userId,

    @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    UUID channelId,

    @Schema(description = "마지막으로 읽은 시각", example = "2026-02-20T14:00:00Z")
    Instant lastReadAt
) {

}