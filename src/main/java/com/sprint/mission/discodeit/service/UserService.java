package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  // Create
  UserDto create(UserCreateRequest request, MultipartFile file) throws IOException;

  // Read
  UserDto findById(UUID id);

  // ReadAll
  List<UserDto> findAll();

//    List<UserInfoDto> findAllByChannelId(UUID channel);

  // Update
  UserDto update(UUID id, UserUpdateRequest request, MultipartFile file) throws IOException;

  // Delete
  void delete(UUID id);

}
