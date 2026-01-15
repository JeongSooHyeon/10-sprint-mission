package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // Create
    User create(String name, UserStatus status);

    // Read
    User findById(UUID id);

    // ReadAll
    List<User> readAll();

    // Update
    User update(UUID id, String newName, UserStatus newStatus);

    // User가 보낸 모든 메시지 조회
    void printUserMessages(UUID id);

    // 내가 속한 채널들 조회
    void printUserChannels(UUID id);

    // Delete
    void delete(UUID id);

}
