package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserCreateRequest(
        String userName,
        String email,
        String password,
        byte[] imageBytes
        ) {}
