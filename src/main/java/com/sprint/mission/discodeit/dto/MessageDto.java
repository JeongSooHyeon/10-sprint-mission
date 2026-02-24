package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "메시지 상세 정보 응답 DTO")
public record MessageDto(
    @Schema(description = "메시지 ID", example = "3f3fb215-32aa-4281-904c-45b9dd8b96fb")
    UUID id,

    @Schema(description = "발신자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID authorId,

    @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    UUID channelId,

    @Schema(description = "메시지 내용", example = "달선이라개.")
    String content,

    @Schema(description = "마지막 수정 시각", example = "2026-02-20T15:00:00Z")
    Instant createdAt,

    @Schema(description = "첨부된 바이너리 콘텐츠(파일) ID 목록")
    List<UUID> attachmentIds
) {

}