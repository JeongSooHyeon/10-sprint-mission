package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {
    private final UUID userId;
    private UserStatusEnum userStatusEnum;

    protected UserStatus(User user) {
        super(UUID.randomUUID(), Instant.now());
        this.userId = user.getId();
        this.userStatusEnum = UserStatusEnum.ONLINE;
    }

    public boolean isOnline() {
        Instant now = Instant.now();
        Instant beforeFiveMinute = now.minus(Duration.ofMinutes(5));
        if (updatedAt.isBefore(beforeFiveMinute)) {    // 마지막 접속 시간이 5분 전이면
            return false;   // 오프라인
        }
        return true;    // 온라인
    }

    public void updateUserStatus(){
        if (isOnline()) {
            this.userStatusEnum = UserStatusEnum.ONLINE;
        }else{
            this.userStatusEnum = UserStatusEnum.OFFLINE;
        }
    }

    // 마지막 접속 시간 갱신
    public void updateLastActiveTime(){
        updateUpdatedAt();
    }
}
