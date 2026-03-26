package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(

    @NotBlank(message = "채널명은 필수입니다.")
    String name,
    @NotBlank(message = "설명은 필수입니다.")
    String description
) {

}
