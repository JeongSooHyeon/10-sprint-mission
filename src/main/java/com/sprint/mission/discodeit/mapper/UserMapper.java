package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


  //  User -> UserInfoDto
  public UserDto toUserInfoDto(User user, UserStatus userStatus) {
    return new UserDto(user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getName(),
        userStatus.getLastActiveAt(),
        user.getEmail(),
        user.getProfileId(),
        userStatus.isOnline());
  }

  // UserInfoDto -> User
  public User toUser(UserDto userDto, UserRepository userRepository) {
    String password = userRepository.findById(userDto.id())
        .map(User::getPassword)
        .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자 정보가 없습니다."));
    return new User(userDto.username(), userDto.email(), password,
        userDto.profileId());
  }
}
