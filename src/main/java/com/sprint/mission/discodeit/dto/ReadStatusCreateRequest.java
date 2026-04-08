package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "User의 id는 필수입니다.")
    UUID userId,
    @NotNull(message = "Channel의 id는 필수입니다.")
    UUID channelId,
    Instant lastReadAt
) {

}
