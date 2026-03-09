package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage storage;


  @Override
  @Transactional
  public UserDto create(UserCreateRequest request, MultipartFile file) throws IOException {
    userRepository.findByUsername(request.username())
        .ifPresent(u -> {
          throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        });
    userRepository.findByEmail(request.email())
        .ifPresent(u -> {
          throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });

    BinaryContent profile = null;
    if (file != null && !file.isEmpty()) {
      profile = new BinaryContent(
          file.getContentType(),
          file.getSize(),
          file.getOriginalFilename()
      );

      binaryContentRepository.save(profile);

      storage.put(profile.getId(), file.getBytes());
    }
    User user = new User(request.username(), request.email(), request.password(),
        profile);

    UserStatus userStatus = new UserStatus(user, Instant.now());
    user.setUserStatus(userStatus);
    userStatus.setUser(user);
    userRepository.save(user);

    return userMapper.toUserDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findById(UUID id) {
    User user = userRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("실패 : 존재하지 않는 사용자 ID입니다."));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    return userMapper.toUserDto(user);

  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();

    return users.stream()
        .map(userMapper::toUserDto)
        .toList();
  }

  @Override
  @Transactional
  public UserDto update(UUID id, UserUpdateRequest request, MultipartFile file) throws IOException {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    userRepository.findByUsername(request.newUsername())
        .ifPresent(u -> {
          throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        });
    userRepository.findByEmail(request.newEmail())
        .ifPresent(u -> {
          throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });

    if (user.getStatus() == null) {
      UserStatus newStatus = new UserStatus(user, Instant.now());
      user.setUserStatus(newStatus);
    }

    BinaryContent profile = user.getProfile();

    if (file != null && !file.isEmpty()) {
      profile = new BinaryContent(
          file.getContentType(),
          file.getSize(),
          file.getOriginalFilename()
      );

      binaryContentRepository.save(profile);

      storage.put(profile.getId(), file.getBytes());
    }

    user.update(request.newUsername(), profile, request.newEmail(), request.newPassword());

    updateLastActiveTime(user.getId());   // 마지막 접속 시간 갱신

    return userMapper.toUserDto(user);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

    // 사용자가 작성한 메시지 삭제
    messageRepository.deleteByAuthorId(id);

    // 사용자의 ReadStatus 삭제
    readStatusRepository.deleteByUserId(id);

    userRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void updateLastActiveTime(UUID id) {
    Optional<UserStatus> userStatus = userStatusRepository.findByUserId(id);
    userStatus.ifPresent(us -> {
      us.update(Instant.now());
    });
  }
}
