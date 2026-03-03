package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  // Create
  ChannelDto createPublic(PublicChannelCreateDto publicChannelCreateDto);

  ChannelDto createPrivate(PrivateChannelCreateDto privateChannelCreateDto);

  // Read
  ChannelDto findById(UUID id);

  // ReadAll
  List<ChannelDto> findAllByUserId(UUID userId);

  // Update
  ChannelDto update(UUID id, ChannelUpdateDto channelUpdateDto);

  // 채널 참여
  ChannelDto joinChannel(UUID userId, UUID channelId);


  // Delete
  void delete(UUID id);
}
