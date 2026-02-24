package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

  // Message -> MessageInfoDto
  public MessageDto toMessageInfoDto(Message message) {
    return new MessageDto(message.getId(), message.getSenderId(), message.getChannelId(),
        message.getContent(), message.getUpdatedAt(), message.getAttachmentIds());
  }
}
