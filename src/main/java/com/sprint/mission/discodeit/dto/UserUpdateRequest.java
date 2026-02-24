package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserUpdateRequest(
    String newUsername,
    UUID newProfileId,
    String newEmail,
    String newPassword
) {

}
