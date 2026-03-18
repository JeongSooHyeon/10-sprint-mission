package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BinaryContentMapper.class})
public abstract class MessageMapper {

  @Autowired
  protected BinaryContentMapper binaryContentMapper;
  @Autowired
  protected UserMapper userMapper;

  @Mapping(target = "author", source = "author")
  @Mapping(source = ".", target = "attachments", qualifiedByName = "getAttachments")
  // default_batch_fetch_size 설정
  @Mapping(source = "channel.id", target = "channelId")
  public abstract MessageDto toMessageDto(Message message);


  @Named("getAttachments")
  protected List<BinaryContentDto> getAttachments(Message message) {
    if (message.getAttachments() == null) {
      return List.of();
    }

    return message.getAttachments().stream()
        .map(bc -> binaryContentMapper.toBinaryContentDto(bc))
        .toList();
  }
}
