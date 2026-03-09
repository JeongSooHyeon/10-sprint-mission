package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.IsPrivate;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "채널 정보 응답 DTO")
public record ChannelDto(
    @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    UUID id,

    @Schema(description = "공개 여부 (PUBLIC: 공개, PRIVATE: 비공개)", example = "PUBLIC")
    IsPrivate type,

    @Schema(description = "채널 이름", example = "달멍이들의 채널")
    String name,

    @Schema(description = "채널 설명", example = "인간들은 들어올 수 없다.")
    String description,

    @Schema(description = "채널에 참여 중인 사용자 목록")
    List<UserDto> participants,

    @Schema(description = "해당 채널의 마지막 메시지 발송 시간 (메시지가 없으면 null)", example = "2026-03-05T18:10:00Z")
    Instant lastMessageAt
) {

}