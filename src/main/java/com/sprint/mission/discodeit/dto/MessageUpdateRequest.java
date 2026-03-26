package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record MessageUpdateRequest(
    @NotBlank(message = "메시지는 공백일 수 없습니다.")
    @Size(max = 5000, message = "메시지가 너무 깁니다.")
    String newContent

) {

}
