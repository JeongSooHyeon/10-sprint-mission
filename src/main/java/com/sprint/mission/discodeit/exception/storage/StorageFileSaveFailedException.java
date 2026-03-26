package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class StorageFileSaveFailedException extends StorageException {

  public StorageFileSaveFailedException(UUID id) {
    super(ErrorCode.FILE_SAVE_FAILED, Map.of("id", id));
  }
}
