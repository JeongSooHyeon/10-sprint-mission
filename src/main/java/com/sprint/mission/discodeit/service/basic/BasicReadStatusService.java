package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  public ReadStatusDto create(ReadStatusCreateDto readStatusCreateDto) {
    // Channel, User 존재 여부 검증
    userRepository.findById(readStatusCreateDto.userId())
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

    channelRepository.findById(readStatusCreateDto.channelId())
        .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다."));

    // 중복된 데이터 검증
    return readStatusRepository.findByUserIdAndChannelId(readStatusCreateDto.userId(),
            readStatusCreateDto.channelId())
        .map(readStatusMapper::toReadStatusInfoDto) // 이미 있으면 그걸 그냥 리턴 (200 OK)
        .orElseGet(() -> {
          // 2. 진짜 없으면 그때 생성한다.
          ReadStatus newStatus = new ReadStatus(readStatusCreateDto.userId(),
              readStatusCreateDto.channelId());
          return readStatusMapper.toReadStatusInfoDto(readStatusRepository.save(newStatus));
        });
  }

  @Override
  public ReadStatusDto findById(UUID id) {
    return readStatusMapper
        .toReadStatusInfoDto(
            readStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ReadStatus가 없습니다.")));
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toReadStatusInfoDto)
        .toList();
  }

  @Override
  public ReadStatusDto update(UUID id, ReadStatusUpdateDto readStatusUpdateDto) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 ReadStatus가 없습니다."));
    readStatus.updateLastReadAt(readStatusUpdateDto.newLastReadAt());
    readStatusRepository.save(readStatus);
    return readStatusMapper.toReadStatusInfoDto(readStatus);
  }

  @Override
  public void delete(UUID id) {
    readStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 ReadStatus가 없습니다."));
    readStatusRepository.delete(id);
  }


}
