package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class StorageInitFailedException extends StorageException {

  public StorageInitFailedException(String path) {
    super(ErrorCode.STORAGE_INIT_FAILED, Map.of("path", path));
  }
}
