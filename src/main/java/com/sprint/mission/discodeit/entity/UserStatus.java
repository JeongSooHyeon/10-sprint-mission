package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {

  private final UUID userId;
  private Instant lastActiveAt;


  public UserStatus(UUID userId, Instant lastActiveAt) {
    super(UUID.randomUUID(), Instant.now());
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  public boolean isOnline() {
    Instant now = Instant.now();
    Instant beforeFiveMinute = now.minus(Duration.ofMinutes(5));
    return updatedAt.isAfter(beforeFiveMinute);    // 마지막 접속 시간이 5분 전이면
  }

}
