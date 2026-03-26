package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UserUpdateRequest(
    @Size(max = 50, message = "이름은 50자 이하여야 합니다.")
    String newUsername,

    @Email(message = "유효한 이메일 형식이어야 합니다.")
    String newEmail,

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    String newPassword
) {

}
