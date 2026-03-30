package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.IsPrivate;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @InjectMocks
  private BasicMessageService messageService;

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private PageResponseMapper pageResponseMapper;

  @Test
  @DisplayName("메시지 생성 성공 - 첨부파일 없음")
  void create_success_withoutAttachments() throws IOException {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest(authorId, channelId, "안녕하세요");
    User author = new User("달선", "dalsun@naver.com", "ekftjs123", null);
    Channel channel = new Channel("공지방", IsPrivate.PUBLIC, "공지방입니다.");
    MessageDto dto = new MessageDto(messageId, Instant.now(), null, "안녕하세요", UUID.randomUUID(),
        null, null);

    when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(userStatusRepository.findByUserId(authorId)).thenReturn(Optional.empty());
    when(readStatusRepository.findByUserIdAndChannelId(authorId, channel.getId())).thenReturn(
        Optional.empty());
    when(messageMapper.toMessageDto(any(Message.class))).thenReturn(dto);

    // when
    MessageDto result = messageService.create(request, null);

    // then
    assertNotNull(result);
    assertEquals("안녕하세요", result.content());
    verify(messageRepository).save(any(Message.class));
    verify(binaryContentRepository, never()).save(any(BinaryContent.class));
  }

  @Test
  @DisplayName("메시지 생성 실패 - 존재하지 않는 사용자")
  void create_fail_userNotFound() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest(authorId, channelId, "안녕하세요");

    when(userRepository.findById(authorId)).thenReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> messageService.create(request, null))
        .isInstanceOf(UserNotFoundException.class);

    verify(messageRepository, never()).save(any(Message.class));
  }

  @Test
  @DisplayName("채널 메시지 목록 조회 성공")
  void findAllByChannelId_success_readStatusNotExists() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    User user = new User("달선", "dalsun@naver.com", "ekftjs123", null);
    Channel channel = new Channel("공지방", IsPrivate.PUBLIC, "공지방입니다.");
    Slice<Message> messageSlice = mock(Slice.class);
    PageResponse<MessageDto> pageResponse = new PageResponse<>(List.of(), null, 10, false, null);

    when(readStatusRepository.findByUserIdAndChannelId(userId, channelId))
        .thenReturn(Optional.empty());
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(messageRepository.findAllByChannelId(eq(channelId), any(Pageable.class)))
        .thenReturn(messageSlice);
    when(pageResponseMapper.fromSlice(any(Slice.class))).thenReturn(pageResponse);

    // when
    PageResponse<MessageDto> result = messageService.findAllByChannelId(userId, channelId, null,
        Pageable.unpaged());

    // then
    assertNotNull(result);
    verify(readStatusRepository).save(any(ReadStatus.class));
  }

  @Test
  @DisplayName("채널 메시지 목록 조회 실패 - 존재하지 않는 사용자")
  void findAllByChannelId_fail_userNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    when(readStatusRepository.findByUserIdAndChannelId(userId, channelId))
        .thenReturn(Optional.empty());
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // when, then
    assertThatThrownBy(
        () -> messageService.findAllByChannelId(userId, channelId, null, Pageable.unpaged()))
        .isInstanceOf(IllegalArgumentException.class);

    verify(readStatusRepository, never()).save(any(ReadStatus.class));
    verify(messageRepository, never()).findAllByChannelId(any(UUID.class), any(Pageable.class));
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void update_success() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");
    Message message = new Message(
        new User("달선", "dalsun@naver.com", "ekftjs123", null),
        new Channel("공지방", IsPrivate.PUBLIC, "공지방입니다."),
        "안녕하세요",
        new ArrayList<>()
    );
    MessageDto dto = new MessageDto(messageId, Instant.now(), null, "수정된 내용", UUID.randomUUID(),
        null, null);

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    when(messageMapper.toMessageDto(any(Message.class))).thenReturn(dto);

    // when
    MessageDto result = messageService.update(messageId, request);

    // then
    assertNotNull(result);
    assertEquals("수정된 내용", result.content());
  }

  @Test
  @DisplayName("메시지 수정 실패 - 존재하지 않는 메시지")
  void update_fail_messageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> messageService.update(messageId, request))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void delete_success() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = new Message(
        new User("달선", "dalsun@naver.com", "ekftjs123", null),
        new Channel("공지방", IsPrivate.PUBLIC, "공지방입니다."),
        "안녕하세요",
        new ArrayList<>()
    );

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

    // when
    messageService.delete(messageId);

    // then
    verify(messageRepository).deleteById(messageId);
  }

  @Test
  @DisplayName("메시지 삭제 실패 - 존재하지 않는 메시지")
  void delete_fail_messageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> messageService.delete(messageId))
        .isInstanceOf(MessageNotFoundException.class);

    verify(messageRepository, never()).deleteById(any());
  }
}