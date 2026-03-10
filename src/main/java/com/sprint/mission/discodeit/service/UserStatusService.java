package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateDto userStatusCreateDto);

  UserStatusDto findById(UUID id);

  List<UserStatusDto> findAll();

  UserStatusDto update(UUID userStatusId, UserStatusUpdateByIdDto userStatusUpdateByIdDto);

  UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest);

  void delete(UUID id);

}
