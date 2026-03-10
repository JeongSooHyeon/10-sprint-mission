package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final ReadStatusRepository readStatusRepository;
  private final PageResponseMapper pageResponseMapper;
  private final BinaryContentStorage binaryContentStorage;
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments)
      throws IOException {
    User author = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new IllegalArgumentException("일치하는 채널이 없습니다."));

    List<BinaryContent> savedContents = new ArrayList<>();
    if (attachments != null) {
      for (MultipartFile file : attachments) {
        BinaryContent content = new BinaryContent(file.getContentType(), file.getSize(),
            file.getOriginalFilename());

        binaryContentRepository.save(content);
        entityManager.flush();

        binaryContentStorage.put(content.getId(), file.getBytes());
        savedContents.add(content);
      }
    }

    Instant now = Instant.now();
    Message message = new Message(author, channel,
        messageCreateRequest.content(), savedContents);
    messageRepository.save(message);

    // 사용자 활동 시간 갱신
    userStatusRepository.findByUserId(messageCreateRequest.authorId())
        .ifPresent(us -> us.update(now));

    readStatusRepository.findByUserIdAndChannelId(messageCreateRequest.authorId(),
            channel.getId())
        .ifPresent(rs -> rs.updateLastReadAt(now.plusMillis(10))); // 찰나의 차이로 본인 메시지 안 읽음 방지
    return messageMapper.toMessageDto(message);
  }

  private void validateAttachments(List<BinaryContent> attachments) {
    if (attachments == null) {
      return;   // 첨부파일 없을 때
    }

    boolean allMatch = attachments.stream()   // 첨부파일이 유효할 때
        .allMatch(bc -> binaryContentRepository.findById(bc.getId()).isPresent());

    if (!allMatch) {   // 첨부파일 유효 X
      throw new IllegalArgumentException("해당 파일이 없습니다.");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto findById(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메시지 ID입니다."));

    return messageMapper.toMessageDto(message);
  }

//  @Override
//  @Transactional
//  public PageResponse<MessageDto> findAllByChannelId(UUID userId, UUID channelId, UUID cursor,
//      Pageable pageable) {
//
//    readStatusRepository.findByUserIdAndChannelId(userId, channelId)
//        .ifPresentOrElse(
//            rs -> rs.updateLastReadAt(Instant.now()),
//            () -> {
//              // 없으면(Public 채널 첫 방문 등) 새로 생성
//              User user = userRepository.findById(userId)
//                  .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
//              Channel channel = channelRepository.findById(channelId)
//                  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
//              readStatusRepository.save(new ReadStatus(user, channel, Instant.now()));
//            }
//        );
//
//    Slice<Message> messageSlice = messageRepository.findAllByChannelId(channelId, pageable);
//    return pageResponseMapper.fromSlice(messageSlice.map(messageMapper::toMessageDto));
//  }

  @Override
  @Transactional
  public MessageDto update(UUID id, MessageUpdateRequest messageUpdateRequest) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 메시지가 없습니다."));

    message.updateContent(messageUpdateRequest.newContent());

    return messageMapper.toMessageDto(message);
  }

//  @Override
//  @Transactional
//  public PageResponse<MessageDto> searchMessage(UUID userId, UUID channelId, String keyword,
//      Pageable pageable) {
//    channelRepository.findById(channelId)
//        .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다."));
//
//    Slice<Message> messageSlice = messageRepository.findByChannelIdAndContentContaining(channelId,
//        keyword, pageable);
//    return pageResponseMapper.fromSlice(messageSlice.map(messageMapper::toMessageDto));
//  }
//
//  @Override
//  @Transactional(readOnly = true)
//  public PageResponse<MessageDto> getUserMessages(UUID id, Pageable pageable) {
//    Slice<Message> messageSlice = messageRepository.findAllByUserId(id, pageable);
//    return pageResponseMapper.fromSlice(messageSlice.map(messageMapper::toMessageDto));
//  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> getMessages(UUID channelId, Instant cursor, Pageable pageable) {
    Slice<Message> messageSlice = (cursor == null)
        ? messageRepository.findAllByChannelId(channelId, pageable)
        : messageRepository.findAllByChannelIdBeforeCursor(channelId, cursor, pageable);

    List<MessageDto> content = messageSlice.getContent().stream()
        .map(messageMapper::toMessageDto)
        .toList();

    Instant nextCursor = messageSlice.hasNext() && !content.isEmpty()
        ? content.get(content.size() - 1).createdAt()
        : null;

    return new PageResponse<>(
        content,
        nextCursor,
        messageSlice.getSize(),
        messageSlice.hasNext(),
        null
    );
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 메시지가 없습니다."));

    // 메시지 자체 삭제
    messageRepository.deleteById(id);
  }
}
