package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserLoginDto;

public interface AuthService {

  UserDto login(UserLoginDto request);
}
