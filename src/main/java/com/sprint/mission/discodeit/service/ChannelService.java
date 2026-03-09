package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  // Create
  ChannelDto createPublic(PublicChannelCreateRequest publicChannelCreateRequest);

  ChannelDto createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest);

  // Read
  ChannelDto findById(UUID id);

  // ReadAll
  List<ChannelDto> findAllByUserId(UUID userId);

  // Update
  ChannelDto update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest);

  // 채널 참여
  ChannelDto joinChannel(UUID userId, UUID channelId);


  // Delete
  void delete(UUID id);
}
