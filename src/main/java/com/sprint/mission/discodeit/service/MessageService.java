package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  // Create
  MessageDto create(MessageCreateRequest messageCreateRequest, List<MultipartFile> attachments)
      throws IOException;

  // Read
  MessageDto findById(UUID id);

  // ReadAll
//  PageResponse<MessageDto> findAllByChannelId(UUID userId, UUID channelId, UUID cursor,
//      Pageable pageable);

  // Update
  MessageDto update(UUID id, MessageUpdateRequest messageUpdateRequest);

//  PageResponse<MessageDto> searchMessage(UUID userId, UUID channelId, String keyword,
//      Pageable pageable);

//  PageResponse<MessageDto> getUserMessages(UUID id, Pageable pageable);

//    List<MessageResponseDto> getChannelMessages(UUID channel);

//    UUID sendDirectMessage(UUID authorId, UUID receiverId, String bytes);

  PageResponse<MessageDto> getMessages(UUID channelId, Instant cursor, Pageable pageable);

  // Delete
  void delete(UUID id);

}
