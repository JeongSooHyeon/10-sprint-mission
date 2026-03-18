package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.IsPrivate;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

  @Autowired
  protected MessageRepository messageRepository;

  @Autowired
  protected ReadStatusRepository readStatusRepository;

  @Autowired
  protected UserMapper userMapper;

  @Mapping(source = ".", target = "name", qualifiedByName = "getNameByPrivate")
  @Mapping(source = ".", target = "description", qualifiedByName = "getDescriptionByPrivate")
  @Mapping(source = ".", target = "participants", ignore = true)
  @Mapping(source = ".", target = "lastMessageAt", ignore = true)
  public abstract ChannelDto toChannelDto(Channel channel);

  public ChannelDto toChannelDto(Channel channel, List<UserDto> participants,
      Instant lastMessageAt) {
    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        getNameByPrivate(channel),
        getDescriptionByPrivate(channel),
        participants,
        lastMessageAt
    );
  }

  @Named("getNameByPrivate")
  protected String getNameByPrivate(Channel channel) {
    return (channel.getType() == IsPrivate.PRIVATE) ? null : channel.getName();
  }

  @Named("getDescriptionByPrivate")
  protected String getDescriptionByPrivate(Channel channel) {
    return (channel.getType() == IsPrivate.PRIVATE) ? null : channel.getDescription();
  }

  // participants 가져오기
  @Named("getParticipants")
  protected List<UserDto> getParticipants(Channel channel) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channel.getId());
    return readStatuses.stream()
        .map(ReadStatus::getUser)
        .map(u -> userMapper.toUserDto(u))
        .toList();
  }

  @Named("getLastMessageAt")
  protected Instant getLastMessageAt(Channel channel) {
    // 마지막 메시지 시간
    Instant lastMessageAt = null;
    Message lastMessage = messageRepository.findFirstByChannelIdOrderByCreatedAtDesc(
        channel.getId()).orElse(null);
    if (lastMessage != null) {
      lastMessageAt = lastMessage.getCreatedAt();
    }
    return lastMessageAt;
  }


}
