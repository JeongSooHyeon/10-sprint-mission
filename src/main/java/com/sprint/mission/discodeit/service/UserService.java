package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserInfo;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.*;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // Create
    User create(UserCreateRequest request);

    // Read
    UserInfo findById(UUID id);

    // ReadAll
    List<UserInfo> findAll();

    // Update
    User update(UserUpdateRequest request);

    // User가 보낸 모든 메시지 조회
    List<Message> getUserMessages(UUID id);

    // 내가 속한 채널들 조회
    List<Channel> getUserChannels(UUID id);

    // Delete
    void delete(UUID id);

    public void updateLastActiveTime(UUID id);
}
