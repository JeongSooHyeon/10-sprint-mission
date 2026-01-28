package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BinaryContent extends BaseEntity{
    private final UUID profileId;
    private final List<UUID> attachmentIds;

    protected BinaryContent(User user, Message message) {
        super(UUID.randomUUID(), Instant.now());
        this.profileId = user.getProfileId();
        this.attachmentIds = message.getAttachmentIds();
    }

}
