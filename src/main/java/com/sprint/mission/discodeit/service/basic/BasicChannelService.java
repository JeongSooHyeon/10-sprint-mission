package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserStatusRepository userStatusRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @Override
  @Transactional
  public ChannelDto createPublic(PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel channel =
        new Channel(publicChannelCreateRequest.name(), IsPrivate.PUBLIC,
            publicChannelCreateRequest.description());

    channelRepository.save(channel);
    return channelMapper.toChannelDto(channel);
  }

  @Override
  @Transactional
  public ChannelDto createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel channel =
        new Channel(null, IsPrivate.PRIVATE, null);
    channelRepository.save(channel);

    // ReadStatus 생성
    privateChannelCreateRequest.participantIds()
        .forEach(uId -> {
          User user = userRepository.findById(uId)
              .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
          ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
          readStatusRepository.save(readStatus);
        });

    return channelMapper.toChannelDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto findById(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("실패 : 존재하지 않는 채널 ID입니다."));

    return channelMapper.toChannelDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return channelRepository.findAll().stream()
        .filter(ch -> isVisibleToUser(ch, userId))
        .map(channelMapper::toChannelDto)
        .toList();
  }

  private boolean isVisibleToUser(Channel channel, UUID userId) {
    if (channel.getType().equals(IsPrivate.PUBLIC)) {
      return true;
    }
    return readStatusRepository.findByUserIdAndChannelId(userId, channel.getId()).isPresent();
  }


  @Override
  @Transactional
  public ChannelDto update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다."));
    if (channel.getType().equals(IsPrivate.PRIVATE)) {
      throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
    }
    channel.updateName(publicChannelUpdateRequest.newName());
    channel.updateDescription(publicChannelUpdateRequest.newDescription());
    return channelMapper.toChannelDto(channel);
  }

  @Override
  @Transactional
  public ChannelDto joinChannel(UUID userId, UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다."));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));

    ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
    readStatusRepository.save(readStatus);

    userStatusRepository.findByUserId(userId)
        .ifPresent(status -> status.update(Instant.now()));

    return channelMapper.toChannelDto(channel);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다."));

    // 채널의 메시지 삭제하기
    messageRepository.deleteByChannelId(id);

    // ReadStatus 삭제
    readStatusRepository.deleteByChannelId(id);
    channelRepository.deleteById(id);
  }

}
