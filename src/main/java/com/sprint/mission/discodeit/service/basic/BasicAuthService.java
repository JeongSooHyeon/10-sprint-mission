package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserLoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto login(UserLoginDto request) {
    System.out.println(userRepository.findAll());
    User user = userRepository.findByName(request.username())
        .filter(u -> u.getPassword().equals(request.password()))
        .orElseThrow(() -> new IllegalArgumentException("해당 정보와 일치하는 사용자가 없습니다."));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseGet(() -> new UserStatus(user.getId(), Instant.now()));
    userStatus.update(Instant.now());
    userStatusRepository.save(userStatus);
    return userMapper.toUserInfoDto(user, userStatus);
  }
}