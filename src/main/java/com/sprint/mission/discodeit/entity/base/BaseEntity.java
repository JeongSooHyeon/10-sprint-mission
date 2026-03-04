package com.sprint.mission.discodeit.entity.base;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  protected UUID id;
  protected Instant createdAt;

}
