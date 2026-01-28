package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity{
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(User user, Channel channel) {
        super(UUID.randomUUID(), Instant.now());
        this.userId = user.getId();
        this.channelId = channel.getId();
        lastReadAt = Instant.MIN;
    }

    public void updateLastReadAt(){
        lastReadAt = Instant.now();
    }

    public boolean isUnread(Instant messageCreatedAt){
        return lastReadAt.isBefore(messageCreatedAt);
    }
}
