package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidCredentialException extends UserException {

  public InvalidCredentialException(String username) {
    super(ErrorCode.INVALID_CREDENTIALS, Map.of("username", username));
  }
}
