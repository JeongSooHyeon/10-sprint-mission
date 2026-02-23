package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record BinaryContentResponseDto(
    @Schema(description = "바이너리 데이터 ID", example = "3f3fb215-32aa-4281-904c-45b9dd8b96fb")
    UUID id,

    @Schema(description = "콘텐츠 타입", example = "image/jpeg")
    String contentType,

    @Schema(description = "데이터 바이트 배열", example = "binary")
    byte[] bytes,

    Long size,

    String fileName
) {

}
