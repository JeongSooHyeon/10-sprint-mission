package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Getter
@NoArgsConstructor
public class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  @Column(nullable = false)
  protected Instant updatedAt;

  public BaseUpdatableEntity(UUID id) {
    super(id);
  }

}
