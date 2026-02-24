package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  // Create
  MessageDto create(MessageCreateDto messageCreateDto, List<UUID> attachmentIds);

  // Read
  MessageDto findById(UUID id);

  // ReadAll
  List<MessageDto> findAllByChannelId(UUID channelId);

  // Update
  MessageDto update(UUID id, MessageUpdateDto messageUpdateDto);

  List<MessageDto> searchMessage(UUID channelId, String keyword);

  List<MessageDto> getUserMessages(UUID id);

//    List<MessageResponseDto> getChannelMessages(UUID channelId);

//    UUID sendDirectMessage(UUID authorId, UUID receiverId, String bytes);

  // Delete
  void delete(UUID id);

}
