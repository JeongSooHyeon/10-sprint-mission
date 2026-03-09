package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record BinaryContentDto(
    @Schema(description = "바이너리 데이터 ID", example = "3f3fb215-32aa-4281-904c-45b9dd8b96fb")
    UUID id,

    @Schema(description = "파일 이름", example = "웃긴달선.jpg")
    String fileName,

    @Schema(description = "파일 크기", example = "154544")
    Long size,

    @Schema(description = "콘텐츠 타입", example = "image/jpeg")
    String contentType

) {

}
