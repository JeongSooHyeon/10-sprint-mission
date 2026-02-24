package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateDto readStatusCreateDto);

  ReadStatusDto findById(UUID uuid);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(UUID id, ReadStatusUpdateDto readStatusUpdateDto);

  void delete(UUID id);
}
