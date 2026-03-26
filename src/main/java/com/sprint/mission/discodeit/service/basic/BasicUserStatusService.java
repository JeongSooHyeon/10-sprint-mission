package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service

public class BasicUserStatusService implements UserStatusService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserStatusDto create(UserStatusCreateDto userStatusCreateDto) {
    User user = userRepository.findById(userStatusCreateDto.userId())
        .orElseThrow(() -> new UserNotFoundException(userStatusCreateDto.userId()));
    userStatusRepository.findByUserId(user.getId())
        .ifPresent(i -> {
          throw new UserStatusAlreadyExistsException(user.getId());
        });

    UserStatus userStatus = new UserStatus(user, userStatusCreateDto.lastActiveAt());
    userStatusRepository.save(userStatus);
    return userStatusMapper.toUserStatusDto(userStatus);
  }

//  @Override
//  @Transactional(readOnly = true)
//  public UserStatusDto findById(UUID id) {
//    return userStatusMapper.toUserStatusDto(userStatusRepository.findById(id)
//        .orElseThrow(() -> new UserStatusNotFoundException(id)));
//  }

//  @Override
//  @Transactional(readOnly = true)
//  public List<UserStatusDto> findAll() {
//    return userStatusRepository.findAll().stream()
//        .map(userStatusMapper::toUserStatusDto)
//        .toList();
//  }

//  @Override
//  @Transactional
//  public UserStatusDto update(UUID userStatusId,
//      UserStatusUpdateByIdDto userStatusUpdateByIdDto) {
//    UserStatus userStatus = userStatusRepository.findById(userStatusId)
//        .orElseThrow(() ->
//
//    userStatus.update(userStatusUpdateByIdDto.newLastActiveAt());
//    return userStatusMapper.toUserStatusDto(userStatus);
//  }

  // User 정보를 포함하여 반환
  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException(userId));

    userStatus.update(userStatusUpdateRequest.newLastActiveAt());
    return userStatusMapper.toUserStatusDto(userStatus);
  }

//  @Override
//  @Transactional
//  public void delete(UUID id) {
//    userStatusRepository.findById(id)
//        .orElseThrow(() -> new UserStatusAlreadyExistsException(id));
//    userStatusRepository.deleteById(id);
//  }
}
