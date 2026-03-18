package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentMapper INSTANT = Mappers.getMapper(BinaryContentMapper.class);

  BinaryContentDto toBinaryContentDto(BinaryContent binaryContent);

}