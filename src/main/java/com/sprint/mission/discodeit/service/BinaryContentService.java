package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDto create(BinaryContentCreateDto binaryContentCreateDto);

  BinaryContentDto findById(UUID id);

  List<BinaryContentDto> findAllById(List<UUID> idList);

  void delete(UUID id);

}
