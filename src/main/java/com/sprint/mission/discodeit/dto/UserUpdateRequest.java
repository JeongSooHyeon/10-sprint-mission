package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserUpdateRequest(
        UUID userId,
        String newName,
        byte[] imageBytes
) {}
