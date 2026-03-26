package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @NotBlank(message = "채널명은 공백일 수 없습니다.")
    @Size(max = 100, message = "채널명은 100글자 이하여야 합니다.")
    String newName,

    @Size(max = 500, message = "설명은 500글자 이하여야 합니다.")
    String newDescription
) {

}
