package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(

    @NotBlank(message = "채널명은 필수입니다.")
    @Size(max = 100, message = "채널명은 100글자 이하여야 합니다.")
    String name,

    @Size(max = 500, message = "설명은 500글자 이하여야 합니다.")
    String description
) {

}
