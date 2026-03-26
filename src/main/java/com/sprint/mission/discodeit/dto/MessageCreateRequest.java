package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MessageCreateRequest(
    @NotNull(message = "발신자 id는 null일 수 없습니다.")
    UUID authorId,

    @NotNull(message = "채널 id는 null일 수 없습니다.")
    UUID channelId,

    @NotBlank(message = "메시지는 공백일 수 없습니다.")
    @Size(max = 5000, message = "메시지가 너무 깁니다.")
    String content
) {

}
