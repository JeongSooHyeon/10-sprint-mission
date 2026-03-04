package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BaseUpdatableEntity extends BaseEntity {

  protected Instant updatedAt;

  public BaseUpdatableEntity(UUID id, Instant createdAt) {
    super(id, createdAt);
    updatedAt = createdAt;
  }

  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }
}
