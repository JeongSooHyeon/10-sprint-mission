package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  // ReadStatus -> ReadStatusInfoDto
  public ReadStatusResponseDto toReadStatusInfoDto(ReadStatus readStatus) {
    return new ReadStatusResponseDto(readStatus.getId(),
        readStatus.getUserId(),
        readStatus.getChannelId(),
        readStatus.getLastReadAt());
  }
}
