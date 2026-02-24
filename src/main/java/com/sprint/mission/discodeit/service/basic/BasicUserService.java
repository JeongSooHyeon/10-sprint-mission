package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ClearMemory;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService, ClearMemory {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto create(UserCreateRequest request) {
    userRepository.findByName(request.username())
        .ifPresent(u -> {
          throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        });
    userRepository.findByEmail(request.email())
        .ifPresent(u -> {
          throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
    User user = new User(request.username(), request.email(), request.password(),
        request.profileId());
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
    userStatusRepository.save(userStatus);
    return userMapper.toUserInfoDto(user, userStatus);
  }

  @Override
  public UserDto findById(UUID id) {
    User user = userRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("실패 : 존재하지 않는 사용자 ID입니다."));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    return userMapper.toUserInfoDto(user, userStatus);

  }

  @Override
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();
    Map<UUID, UserStatus> userStatusMap = userStatusRepository.getUserStatusMap();

    List<UserDto> infoList
        = users.stream()
        .map(u -> {
          UserStatus userStatus = userStatusRepository.findByUserId(u.getId())
              .orElse(null);

          return userMapper.toUserInfoDto(u, userStatusMap.get(u.getId()));
        })
        .toList();
    return infoList;
  }

  @Override
  public UserDto update(UUID id, UserUpdateRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseGet(() -> new UserStatus(id, Instant.now()));

    user = user.update(request);

    updateLastActiveTime(user.getId());   // 마지막 접속 시간 갱신

    userRepository.save(user);
    return userMapper.toUserInfoDto(user, userStatus);
  }

  @Override
  public void delete(UUID id) {
    UserDto userDto = findById(id);

    // 프로필 삭제
    if (userDto.profileId() != null) {
      binaryContentRepository.delete(userDto.profileId());
    }

    // UserStatus 삭제
    userStatusRepository.deleteByUserId(userDto.id());

    // 사용자가 포함된 채널 정리
    channelRepository.deleteByUserId(userDto.id());

    // 사용자가 작성한 메시지 삭제
    messageRepository.deleteByUserId(id);

    // 사용자의 ReadStatus 삭제
    readStatusRepository.deleteByUserId(id);

    userRepository.delete(id);
  }

  @Override
  public void clear() {
    userRepository.clear();
  }

  @Override
  public void updateLastActiveTime(UUID id) {
    Optional<UserStatus> userStatus = userStatusRepository.findByUserId(id);
    userStatus.ifPresent(us -> {
      us.update(Instant.now());
      userStatusRepository.save(us);
    });
  }


}
