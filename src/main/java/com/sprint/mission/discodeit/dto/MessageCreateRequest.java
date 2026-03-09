package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public record MessageCreateRequest(
    UUID authorId,
    UUID channelId,
    String content
) {

}
