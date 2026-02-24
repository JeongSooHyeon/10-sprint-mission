package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

  // Create
  UserDto create(UserCreateRequest request);

  // Read
  UserDto findById(UUID id);

  // ReadAll
  List<UserDto> findAll();

//    List<UserInfoDto> findAllByChannelId(UUID channelId);

  // Update
  UserDto update(UUID id, UserUpdateRequest request);

  // Delete
  void delete(UUID id);

  void updateLastActiveTime(UUID id);
}
