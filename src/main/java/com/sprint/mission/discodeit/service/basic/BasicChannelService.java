package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ClearMemory;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService, ClearMemory {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserStatusRepository userStatusRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @Override
  public ChannelDto createPublic(PublicChannelCreateDto publicChannelCreateDto) {
    Channel channel =
        new Channel(publicChannelCreateDto.name(), IsPrivate.PUBLIC,
            publicChannelCreateDto.description());
    channelRepository.save(channel);
    return channelMapper.toChannelInfoDto(channel, messageRepository);
  }

  @Override
  public ChannelDto createPrivate(PrivateChannelCreateDto privateChannelCreateDto) {
    Channel channel =
        new Channel(null, IsPrivate.PRIVATE, null);

    // ReadStatus 생성
    privateChannelCreateDto.participantIds()
        .forEach(uId -> {
          channel.addUserId(uId);
          ReadStatus readStatus = new ReadStatus(uId, channel.getId());
          readStatusRepository.save(readStatus);
        });

    channelRepository.save(channel);

    return channelMapper.toChannelInfoDto(channel, messageRepository);
  }

  @Override
  public ChannelDto findById(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("실패 : 존재하지 않는 채널 ID입니다."));

    return channelMapper.toChannelInfoDto(channel, messageRepository);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return channelRepository.findAll().stream()
        .filter(ch -> isVisibleToUser(ch, userId))
        .map(ch -> channelMapper.toChannelInfoDto(ch, messageRepository))
        .toList();
  }

  private boolean isVisibleToUser(Channel channel, UUID userId) {
    return channel.getIsPrivate().equals(IsPrivate.PUBLIC)
        || channel.getUserIds().contains(userId);
  }

  @Override
  public ChannelDto update(UUID id, ChannelUpdateDto channelUpdateDto) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다."));
    if (channel.getIsPrivate().equals(IsPrivate.PRIVATE)) {
      throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
    }
    channel.updateName(channelUpdateDto.newName());
    channel.updateDescription(channelUpdateDto.newDescription());
    channelRepository.save(channel);
    return channelMapper.toChannelInfoDto(channel, messageRepository);
  }

  @Override
  public ChannelDto joinChannel(UUID userId, UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다."));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));
    channel.addUserId(user.getId());
    ReadStatus readStatus = new ReadStatus(user.getId(), channel.getId());
    readStatusRepository.save(readStatus);
    channelRepository.save(channel);
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자 상태가 없습니다."));
    userStatus.update(Instant.now());
    userStatusRepository.save(userStatus);
    return channelMapper.toChannelInfoDto(channel, messageRepository);
  }

  @Override
  public void delete(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다."));

    // 채널의 메시지 삭제하기
    messageRepository.deleteByChannelId(id);

    // ReadStatus 삭제
    readStatusRepository.deleteByChannelId(id);
    channelRepository.delete(id);
  }

  @Override
  public void clear() {
    channelRepository.clear();
  }

}
