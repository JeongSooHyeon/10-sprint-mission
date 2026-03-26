package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest) {
    // Channel, User 존재 여부 검증
    User user = userRepository.findById(readStatusCreateRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(readStatusCreateRequest.userId()));

    Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
        .orElseThrow(() -> new ChannelNotFoundException(readStatusCreateRequest.channelId()));

    // 중복된 데이터 검증
    return readStatusRepository.findByUserIdAndChannelId(readStatusCreateRequest.userId(),
            readStatusCreateRequest.channelId())
        .map(readStatusMapper::toReadStatusDto) // 이미 있으면 그걸 그냥 리턴 (200 OK)
        .orElseGet(() -> {
          // 2. 진짜 없으면 그때 생성한다.
          ReadStatus newStatus = new ReadStatus(user,
              channel, readStatusCreateRequest.lastReadAt());
          return readStatusMapper.toReadStatusDto(readStatusRepository.save(newStatus));
        });
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto findById(UUID id) {
    return readStatusMapper
        .toReadStatusDto(
            readStatusRepository.findById(id)
                .orElseThrow(() -> new ReadStatusNotFoundException(id)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toReadStatusDto)
        .toList();
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID id, ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new ReadStatusNotFoundException(id));
    readStatus.updateLastReadAt(readStatusUpdateRequest.newLastReadAt());
    return readStatusMapper.toReadStatusDto(readStatus);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    readStatusRepository.findById(id)
        .orElseThrow(() -> new ReadStatusNotFoundException(id));
    readStatusRepository.deleteById(id);
  }


}
