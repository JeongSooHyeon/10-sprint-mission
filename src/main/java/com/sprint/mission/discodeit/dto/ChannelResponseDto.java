package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.IsPrivate;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "채널 정보 응답 DTO")
public record ChannelResponseDto(
    @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    UUID id,

    @Schema(description = "채널 이름", example = "달멍이들의 채널")
    String name,

    @Schema(description = "공개 여부 (PUBLIC: 공개, PRIVATE: 비공개)", example = "PUBLIC")
    IsPrivate type,

    @Schema(description = "채널 설명", example = "인간들은 들어올 수 없다.")
    String description,

    @Schema(description = "마지막 메시지 전송 시각", example = "2026-02-20T14:30:00Z")
    Instant lastMessageAt,

    @Schema(description = "채널에 참여 중인 유저 ID 목록")
    List<UUID> participantIds
) {

}